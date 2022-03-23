package fr.hyriode.api.impl.common.party;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.party.IHyriPartyManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;

import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPartyManager implements IHyriPartyManager {

    @Override
    public IHyriParty getParty(UUID uuid) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> HyriAPI.GSON.fromJson(this.getJedisKey(uuid), HyriParty.class));
    }

    @Override
    public IHyriParty getPlayerParty(UUID playerUUID) {
        return this.getParty(HyriAPI.get().getPlayerManager().getPlayer(playerUUID).getParty());
    }

    @Override
    public IHyriParty createParty(UUID leader) {
        final IHyriParty party = new HyriParty(UUID.randomUUID(), leader, HyriAPI.get().getServer().getName());

        this.sendParty(party);

        return party;
    }

    @Override
    public void sendParty(IHyriParty party) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.set(this.getJedisKey(party.getId()), HyriAPI.GSON.toJson(party)));
    }

    @Override
    public void removeParty(UUID uuid) {
        final List<UUID> members = this.getMembersInParty(uuid);

        for (UUID member : members) {
            final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
            final IHyriPlayer player = playerManager.getPlayer(member);

            player.setParty(null);

            playerManager.sendPlayer(player);
        }

        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.del(this.getJedisKey(uuid)));
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
    public List<UUID> getMembersInParty(UUID uuid) {
        final IHyriParty party = this.getParty(uuid);

        return party != null ? party.getMembers() : null;
    }

    private String getJedisKey(UUID uuid) {
        return "parties:" + uuid.toString();
    }

}
