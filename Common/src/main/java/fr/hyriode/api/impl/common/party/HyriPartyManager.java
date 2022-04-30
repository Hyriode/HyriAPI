package fr.hyriode.api.impl.common.party;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.party.HyriPartyInvitation;
import fr.hyriode.api.party.HyriPartyRank;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.party.IHyriPartyManager;
import fr.hyriode.api.party.event.HyriPartyCreatedEvent;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;

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
    public IHyriParty getParty(UUID uuid) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            if (uuid != null) {
                final String json = jedis.get(this.getJedisKey(uuid));

                if (json != null) {
                    return HyriAPI.GSON.fromJson(json, HyriParty.class);
                }
            }
            return null;
        });
    }

    @Override
    public IHyriParty getPlayerParty(UUID playerUUID) {
        return this.getParty(HyriAPI.get().getPlayerManager().getPlayer(playerUUID).getParty());
    }

    @Override
    public IHyriParty createParty(UUID leader) {
        final IHyriParty party = new HyriParty(leader, HyriAPI.get().getPlayerManager().getPlayer(leader).getCurrentServer());

        this.sendParty(party);

        HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new HyriPartyCreatedEvent(party));

        return party;
    }

    @Override
    public void sendParty(IHyriParty party) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(this.getJedisKey(party.getId()), HyriAPI.GSON.toJson(party)));
    }

    @Override
    public void removeParty(UUID uuid) {
        final IHyriParty party = this.getParty(uuid);

        if (party != null) {
            for (UUID member : party.getMembers().keySet()) {
                final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
                final IHyriPlayer player = playerManager.getPlayer(member);

                player.setParty(null);
                player.update();
            }

            HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(this.getJedisKey(uuid)));
        }
    }

    @Override
    public void sendInvitation(UUID partyId, UUID sender, UUID player) {
        final HyriPartyInvitation invitation = new HyriPartyInvitation(partyId, sender, player);

        HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
            final String key = INVITATIONS_KEY_GETTER.apply(player, partyId);

            jedis.set(key, HyriAPI.GSON.toJson(invitation));
            jedis.expire(key, 60);
        });

        HyriAPI.get().getPubSub().send(REDIS_CHANNEL, new HyriPartyInvitation.Packet(invitation));
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

    @Override
    public String getPartyServer(UUID uuid) {
        final IHyriParty party = this.getParty(uuid);

        return party != null ? party.getServer() : null;
    }

    @Override
    public UUID getPartyLeader(UUID uuid) {
        final IHyriParty party = this.getParty(uuid);

        return party != null ? party.getLeader() : null;
    }

    @Override
    public Map<UUID, HyriPartyRank> getMembersInParty(UUID uuid) {
        final IHyriParty party = this.getParty(uuid);

        return party != null ? party.getMembers() : null;
    }

    private String getJedisKey(UUID uuid) {
        return REDIS_KEY + uuid.toString();
    }

}
