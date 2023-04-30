package fr.hyriode.api.impl.common.party;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.party.HyriPartyRequest;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.party.IHyriPartyManager;
import fr.hyriode.api.party.event.HyriPartyCreatedEvent;
import fr.hyriode.api.player.IHyriPlayerSession;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPartyManager implements IHyriPartyManager {

    public static final String REDIS_KEY = "parties:";
    public static final String INVITATIONS_KEY = "parties-requests:";

    @Override
    public IHyriParty getParty(@NotNull UUID uuid) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final byte[] bytes = jedis.get((REDIS_KEY + uuid).getBytes(StandardCharsets.UTF_8));

            return bytes != null ? HyriAPI.get().getDataSerializer().deserialize(new HyriParty(), bytes) : null;
        });
    }

    @Override
    public IHyriParty getPlayerParty(UUID playerUUID) {
        final IHyriPlayerSession session = IHyriPlayerSession.get(playerUUID);

        return session == null || session.getParty() == null ? null : this.getParty(session.getParty());
    }

    @Override
    public IHyriParty createParty(@NotNull UUID leader) {
        final IHyriParty party = new HyriParty(leader);

        this.updateParty(party);

        HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new HyriPartyCreatedEvent(party));

        return party;
    }

    @Override
    public void updateParty(@NotNull IHyriParty party) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.set((REDIS_KEY + party.getId()).getBytes(StandardCharsets.UTF_8), HyriAPI.get().getDataSerializer().serialize((HyriParty) party)));
    }

    @Override
    public void removeParty(@NotNull UUID uuid) {
        final IHyriParty party = this.getParty(uuid);

        if (party != null) {
            for (UUID member : party.getMembers().keySet()) {
                final IHyriPlayerSession session = IHyriPlayerSession.get(member);

                if (session == null) {
                    continue;
                }

                session.setParty(null);
                session.update();
            }

            HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.del(REDIS_KEY + uuid));
        }
    }

    @Override
    public void sendRequest(UUID partyId, UUID sender, UUID target) {
        final HyriPartyRequestImpl invitation = new HyriPartyRequestImpl(partyId, sender, target);

        HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
            final Pipeline pipeline = jedis.pipelined();
            final byte[] key = (INVITATIONS_KEY + target.toString() + ":" + partyId.toString()).getBytes(StandardCharsets.UTF_8);

            pipeline.set(key, HyriAPI.get().getDataSerializer().serialize(invitation));
            pipeline.expire(key, 60);
            pipeline.sync();
        });

        HyriAPI.get().getPubSub().send(HyriChannel.PARTIES, invitation);
    }

    @Override
    public void removeRequest(UUID partyId, UUID playerId) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.del(INVITATIONS_KEY + playerId.toString() + ":" + partyId.toString()));
    }

    @Override
    public boolean hasRequest(UUID partyId, UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.exists(INVITATIONS_KEY + playerId.toString() + ":" + partyId.toString()));
    }

    @Override
    public List<HyriPartyRequest> getRequests(UUID player) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final Set<String> keys = jedis.keys(INVITATIONS_KEY + player.toString() + ":*");
            final List<Response<byte[]>> responses = new ArrayList<>();

            final Pipeline pipeline = jedis.pipelined();

            for (String key : keys) {
                responses.add(pipeline.get(key.getBytes(StandardCharsets.UTF_8)));
            }

            pipeline.sync();

            return responses.stream().map(response -> HyriAPI.get().getDataSerializer().deserialize(new HyriPartyRequestImpl(), response.get())).collect(Collectors.toList());
        });
    }

}
