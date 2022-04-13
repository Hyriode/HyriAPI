package fr.hyriode.api.impl.common.party;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.party.*;
import fr.hyriode.api.party.event.*;

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
        final HyriPartyRank oldRank = this.members.get(leader);

        if (oldRank != null) {
            final UUID oldLeader = this.leader;

            this.leader = leader;

            this.members.put(this.leader, HyriPartyRank.LEADER);

            this.triggerEvent(new HyriPartyLeaderEvent(this, this.leader, oldRank, HyriPartyRank.LEADER, oldLeader));
        }
    }

    @Override
    public boolean isLeader(UUID player) {
        return this.leader == player;
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
        if (this.members.get(uuid) == null) {
            this.members.putIfAbsent(uuid, rank);

            this.triggerEvent(new HyriPartyJoinEvent(this, uuid));
        }
    }

    @Override
    public HyriPartyRank promoteMember(UUID uuid) {
        final HyriPartyRank oldRank = this.members.get(uuid);

        if (oldRank != null) {
            final HyriPartyRank newRank = oldRank.getSuperior();

            if (newRank != null) {
                this.members.put(uuid, newRank);

                this.triggerEvent(new HyriPartyPromoteEvent(this, uuid, oldRank, newRank));
            }
        }
        return null;
    }

    @Override
    public HyriPartyRank demoteMember(UUID uuid) {
        final HyriPartyRank oldRank = this.members.get(uuid);

        if (oldRank != null) {
            final HyriPartyRank newRank = oldRank.getInferior();

            if (newRank != null) {
                this.members.put(uuid, newRank);

                this.triggerEvent(new HyriPartyDemoteEvent(this, uuid, oldRank, newRank));
            }
        }
        return null;
    }

    @Override
    public void removeMember(UUID uuid) {
        if (this.members.get(uuid) != null) {
            this.members.remove(uuid);

            this.triggerEvent(new HyriPartyLeaveEvent(this, uuid));
        }
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

        this.triggerEvent(new HyriPartyAccessEvent(this));
    }

    @Override
    public String getServer() {
        return this.server;
    }

    @Override
    public void setServer(String server) {
        final String oldServer = this.server;

        this.server = server;

        this.triggerEvent(new HyriPartyServerEvent(this, oldServer, this.server));
    }

    @Override
    public void sendMessage(String channel, String message, UUID sender, boolean force) {
        for (UUID uuid : this.members.keySet()) {
            HyriAPI.get().getChatChannelManager().sendMessageToPlayer(channel, message, uuid, sender, force);
        }
    }

    @Override
    public void disband(HyriPartyDisbandReason reason) {
        HyriAPI.get().getPartyManager().removeParty(this.id);

        this.triggerEvent(new HyriPartyDisbandEvent(this, reason));
    }

    private void triggerEvent(HyriPartyEvent event) {
        HyriAPI.get().getNetwork().getEventBus().publishAsync(event);
    }

}
