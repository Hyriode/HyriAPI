package fr.hyriode.api.impl.common.party;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.party.HyriPartyInvitation;
import fr.hyriode.api.party.HyriPartyRank;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.party.IHyriPartyManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriParty implements IHyriParty {

    private final UUID id;
    private UUID leader;
    private final long creationDate;
    private final Map<UUID, HyriPartyRank> members;
    private String server;
    private boolean privateParty;

    public HyriParty(UUID leader, String server) {
        this.id = UUID.randomUUID();
        this.leader = leader;
        this.creationDate = System.currentTimeMillis();
        this.members = new HashMap<>();
        this.server = server;
        this.privateParty = true;

        this.members.put(this.leader, HyriPartyRank.LEADER);
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public UUID getLeader() {
        return this.leader;
    }

    @Override
    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    @Override
    public Date getCreationDate() {
        return new Date(this.creationDate);
    }

    @Override
    public Map<UUID, HyriPartyRank> getMembers() {
        return this.members;
    }

    @Override
    public void invitePlayer(UUID sender, UUID uuid) {
        final HyriPartyInvitation invitation = new HyriPartyInvitation(this.id, sender, uuid);

        HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
            final String key = HyriPartyManager.REDIS_KEY + "invitations:" + uuid.toString() + ":" + this.id.toString();

            jedis.set(key, HyriAPI.GSON.toJson(invitation));
            jedis.expire(key, 60);
        });
        HyriAPI.get().getPubSub().send(IHyriPartyManager.REDIS_CHANNEL, new HyriPartyInvitation.Packet(invitation));
    }

    @Override
    public void addMember(UUID uuid, HyriPartyRank rank) {
        this.members.putIfAbsent(uuid, rank);
    }

    @Override
    public HyriPartyRank promoteMember(UUID uuid) {
        final HyriPartyRank newRank = this.members.get(uuid).getSuperior();

        if (newRank != null) {
            this.members.put(uuid, newRank);
        }
        return null;
    }

    @Override
    public HyriPartyRank demoteMember(UUID uuid) {
        final HyriPartyRank newRank = this.members.get(uuid).getInferior();

        if (newRank != null) {
            this.members.put(uuid, newRank);
        }
        return null;
    }

    @Override
    public void removeMember(UUID uuid) {
        this.members.remove(uuid);
    }

    @Override
    public boolean hasMember(UUID uuid) {
        return this.members.containsKey(uuid);
    }

    @Override
    public HyriPartyRank getRank(UUID player) {
        return this.members.get(player);
    }

    @Override
    public boolean isPrivate() {
        return this.privateParty;
    }

    @Override
    public void setPrivate(boolean privateParty) {
        this.privateParty = privateParty;
    }

    @Override
    public String getServer() {
        return this.server;
    }

    @Override
    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public void sendMessage(String channel, String message, UUID sender, boolean force) {
        for (UUID uuid : this.members.keySet()) {
            HyriAPI.get().getChatChannelManager().sendMessageToPlayer(channel, message, uuid, sender, force);
        }
    }

}
