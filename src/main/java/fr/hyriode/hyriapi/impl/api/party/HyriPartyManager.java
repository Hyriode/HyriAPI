package fr.hyriode.hyriapi.impl.api.party;

import com.google.gson.Gson;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.HyriPlugin;
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
 * on 23/07/2021 at 17:52
 */
public class HyriPartyManager implements IHyriPartyManager {

    public static final String REDIS_KEY = "parties:";

    private final HyriPlugin plugin;

    public HyriPartyManager(HyriPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Redis party management
     */

    @Override
    public IHyriParty getParty(UUID uuid) {
        return new Gson().fromJson(this.getJedis().get(this.getJedisKey(uuid)), HyriParty.class);
    }

    @Override
    public IHyriParty getPlayerParty(UUID playerUUID) {
        return this.getParty(this.plugin.getAPI().getPlayerManager().getPlayer(playerUUID).getParty());
    }

    @Override
    public IHyriParty createParty(UUID leader) {
        final IHyriParty party = new HyriParty(UUID.randomUUID(), leader, this.plugin.getAPI().getServer().getId());

        this.sendParty(party);

        return party;
    }

    @Override
    public void sendParty(IHyriParty party) {
        this.getJedis().set(this.getJedisKey(party.getId()), new Gson().toJson(party));
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

        this.getJedis().del(this.getJedisKey(uuid));
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

    private Jedis getJedis() {
        return HyriAPI.get().getJedisResource();
    }

}
