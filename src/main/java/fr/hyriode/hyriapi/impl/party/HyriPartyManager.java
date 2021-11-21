package fr.hyriode.hyriapi.impl.party;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.HyriAPIPlugin;
import fr.hyriode.hyriapi.party.IHyriParty;
import fr.hyriode.hyriapi.party.IHyriPartyManager;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPartyManager implements IHyriPartyManager {

    public static final String REDIS_KEY = "parties:";

    private final HyriAPIPlugin plugin;

    public HyriPartyManager(HyriAPIPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Redis party management
     */

    @Override
    public IHyriParty getParty(UUID uuid) {
        final Jedis jedis = HyriAPI.get().getRedisResource();
        final IHyriParty party = HyriAPIPlugin.GSON.fromJson(this.getJedisKey(uuid), HyriParty.class);

        jedis.close();

        return party;
    }

    @Override
    public IHyriParty getPlayerParty(UUID playerUUID) {
        return this.getParty(this.plugin.getAPI().getPlayerManager().getPlayer(playerUUID).getParty());
    }

    @Override
    public IHyriParty createParty(UUID leader) {
        final IHyriParty party = new HyriParty(UUID.randomUUID(), leader, this.plugin.getAPI().getServer().getName());

        this.sendParty(party);

        return party;
    }

    @Override
    public void sendParty(IHyriParty party) {
        HyriAPI.get().getRedisProcessor().process(jedis -> {
            jedis.set(this.getJedisKey(party.getId()), HyriAPIPlugin.GSON.toJson(party));
            jedis.close();
        });
    }

    @Override
    public void removeParty(UUID uuid) {
        final List<UUID> members = this.getMembersInParty(uuid);

        for (UUID member : members) {
            final IHyriPlayerManager playerManager = this.plugin.getAPI().getPlayerManager();
            final IHyriPlayer player = playerManager.getPlayer(member);

            player.setParty(uuid);

            playerManager.sendPlayer(player);
        }

        HyriAPI.get().getRedisProcessor().process(jedis -> {
            jedis.del(this.getJedisKey(uuid));
            jedis.close();
        });
    }

    /**
     * Other
     */

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
    public List<UUID> getMembersInParty(UUID uuid) {
        final IHyriParty party = this.getParty(uuid);

        return party != null ? party.getMembers() : null;
    }

    private String getJedisKey(UUID uuid) {
        return REDIS_KEY + uuid.toString();
    }

}
