package fr.hyriode.api.impl.common.party;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.channel.HyriChatChannel;
import fr.hyriode.api.party.HyriPartyRank;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.party.event.*;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerSession;

import java.util.*;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriParty implements IHyriParty {

    private final UUID id;
    private UUID leader;

    private final Map<UUID, HyriPartyRank> members;

    private final long creationDate;

    private boolean privateParty;
    private boolean chatEnabled;

    public HyriParty(UUID leader) {
        this.id = UUID.randomUUID();
        this.leader = leader;
        this.members = new HashMap<>();
        this.creationDate = System.currentTimeMillis();
        this.privateParty = true;
        this.chatEnabled = true;

        this.members.put(this.leader, HyriPartyRank.LEADER);

        final IHyriPlayerSession session = IHyriPlayerSession.get(this.leader);

        if (session != null) {
            session.setParty(this.id);
            session.update();
        }
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
            this.members.put(oldLeader, HyriPartyRank.OFFICER);

            this.update();

            this.triggerEvent(new HyriPartyLeaderEvent(this, this.leader, oldRank, HyriPartyRank.LEADER, oldLeader));
        }
    }

    @Override
    public boolean isLeader(UUID player) {
        return this.leader.equals(player);
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
    public List<UUID> getMembers(HyriPartyRank rank) {
        final List<UUID> members = new ArrayList<>();

        for (Map.Entry<UUID, HyriPartyRank> entry : this.members.entrySet()) {
            if (entry.getValue() == rank) {
                members.add(entry.getKey());
            }
        }
        return members;
    }

    @Override
    public void invitePlayer(UUID sender, UUID uuid) {
        HyriAPI.get().getPartyManager().sendInvitation(this.id, sender, uuid);
    }

    @Override
    public void addMember(UUID uuid, HyriPartyRank rank) {
        if (!this.hasMember(uuid)) {
            this.members.putIfAbsent(uuid, rank);

            final IHyriPlayerSession session = IHyriPlayerSession.get(uuid);

            if (session != null) {
                session.setParty(this.id);
                session.update();
            }

            this.update();

            this.triggerEvent(new HyriPartyJoinEvent(this, uuid));
        }
    }

    @Override
    public HyriPartyRank promoteMember(UUID uuid) {
        final HyriPartyRank oldRank = this.members.get(uuid);

        if (oldRank != null) {
            final HyriPartyRank newRank = oldRank.getSuperior();

            if (newRank != null && newRank != HyriPartyRank.LEADER) {
                this.members.put(uuid, newRank);

                this.update();

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

                this.update();

                this.triggerEvent(new HyriPartyDemoteEvent(this, uuid, oldRank, newRank));
            }
        }
        return null;
    }

    @Override
    public void removeMember(UUID uuid, RemoveReason reason) {
        if (this.hasMember(uuid)) {
            this.members.remove(uuid);

            final IHyriPlayerSession session = IHyriPlayerSession.get(uuid);

            if (session != null) {
                session.setParty(null);
                session.update();
            }

            this.update();

            this.triggerEvent(new HyriPartyLeaveEvent(this, uuid, reason));
        }
    }

    @Override
    public void kickMember(UUID uuid, UUID kicker) {
        if (this.hasMember(uuid)) {
            this.members.remove(uuid);

            final IHyriPlayerSession session = IHyriPlayerSession.get(uuid);

            if (session != null) {
                session.setParty(null);
                session.update();
            }

            this.update();

            this.triggerEvent(new HyriPartyKickEvent(this, uuid, kicker));
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

        this.update();

        this.triggerEvent(new HyriPartyAccessEvent(this));
    }

    @Override
    public boolean isChatEnabled() {
        return this.chatEnabled;
    }

    @Override
    public void setChatEnabled(boolean chatEnabled) {
        this.chatEnabled = chatEnabled;

        this.update();

        this.triggerEvent(new HyriPartyChatEvent(this, HyriPartyChatEvent.Action.from(chatEnabled)));
    }

    @Override
    public void sendMessage(UUID sender, String message, boolean component) {
        HyriAPI.get().getChatChannelManager().sendMessage(HyriChatChannel.PARTY, sender, message, component);
    }

    @Override
    public void disband(DisbandReason reason) {
        for (UUID member : this.members.keySet()) {
            final IHyriPlayerSession session = IHyriPlayerSession.get(member);

            if (session != null) {
                session.setParty(null);
                session.update();
            }
        }

        this.triggerEvent(new HyriPartyDisbandEvent(this, reason));
    }

    @Override
    public void teleport(UUID player, UUID target) {
        if (!this.hasMember(player) || !this.hasMember(target)) {
            return;
        }

        final IHyriPlayerSession targetSession = IHyriPlayerSession.get(target);

        if (targetSession == null) {
            return;
        }

        HyriAPI.get().getServerManager().sendPlayerToServer(player, targetSession.getServer());

        this.triggerEvent(new HyriPartyTeleportEvent(this, player, target));
    }

    private void triggerEvent(HyriPartyEvent event) {
        HyriAPI.get().getNetworkManager().getEventBus().publishAsync(event);
    }

}
