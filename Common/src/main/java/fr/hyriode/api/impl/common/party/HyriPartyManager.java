package fr.hyriode.api.impl.common.party;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.party.HyriPartyInvitation;
import fr.hyriode.api.party.HyriPartyRank;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.party.IHyriPartyManager;
import fr.hyriode.api.party.event.HyriPartyCreatedEvent;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.IHyriPlayerSession;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPartyManager implements IHyriPartyManager {

    public static final String REDIS_KEY = "parties:";
    public static final Function<UUID, String> INVITATIONS_KEY = playerId -> REDIS_KEY + "invitations:" + playerId.toString();
    public static final BiFunction<UUID, UUID, String> INVITATIONS_KEY_GETTER = (playerId, partyId) -> INVITATIONS_KEY.apply(playerId) + ":" + partyId.toString();

    @Override
    public IHyriParty getParty(@NotNull UUID uuid) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String json = jedis.get(REDIS_KEY + uuid);

            return json != null ? HyriAPI.GSON.fromJson(json, HyriParty.class) : null;
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
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(REDIS_KEY + party.getId(), HyriAPI.GSON.toJson(party)));
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

            HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(REDIS_KEY + uuid));
        }
    }

    @Override
    public void sendInvitation(UUID partyId, UUID sender, UUID player) {
        final HyriPartyInvitation invitation = new HyriPartyInvitation(partyId, sender, player);

        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final String key = INVITATIONS_KEY_GETTER.apply(player, partyId);

            jedis.set(key, HyriAPI.GSON.toJson(invitation));
            jedis.expire(key, 60);
        });

        HyriAPI.get().getPubSub().send(HyriChannel.PARTIES, new HyriPartyInvitation.Packet(invitation));
    }

    @Override
    public void removeInvitation(UUID partyId, UUID playerId) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.del(INVITATIONS_KEY_GETTER.apply(playerId, partyId)));
    }

    @Override
    public boolean hasInvitation(UUID partyId, UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.get(INVITATIONS_KEY_GETTER.apply(playerId, partyId)) != null);
    }

    @Override
    public List<HyriPartyInvitation> getInvitations(UUID player) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<HyriPartyInvitation> invitations = new ArrayList<>();
            final Set<String> keys = jedis.keys(INVITATIONS_KEY.apply(player) + ":*");

            for (String key : keys) {
                invitations.add(HyriAPI.GSON.fromJson(jedis.get(key), HyriPartyInvitation.class));
            }
            return invitations;
        });
    }

}
