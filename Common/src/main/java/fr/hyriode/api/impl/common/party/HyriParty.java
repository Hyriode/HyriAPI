package fr.hyriode.api.impl.common.party;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.party.HyriPartyRank;
import fr.hyriode.api.party.IHyriParty;

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

    private String server;
    private UUID leader;
    private boolean privateParty;
    private final Date creationDate;
    private final Map<UUID, HyriPartyRank> members;
    private final UUID id;

    public HyriParty(UUID id, UUID leader, String server) {
        this.id = id;
        this.leader = leader;
        this.server = server;
        this.privateParty = true;
        this.members = new HashMap<>();
        this.creationDate = new Date(System.currentTimeMillis());
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
    public IHyriParty setLeader(UUID leader) {
        this.leader = leader;
        return this;
    }

    @Override
    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public Map<UUID, HyriPartyRank> getMembers() {
        return this.members;
    }

    @Override
    public IHyriParty addMember(UUID uuid, HyriPartyRank rank) {
        this.members.put(uuid, rank);
        return this;
    }

    @Override
    public HyriPartyRank promoteMember(UUID uuid) {
        final HyriPartyRank rank = HyriPartyRank.getById(this.members.get(uuid).getId() + 1);
        this.members.put(uuid, rank);
        return rank;
    }

    @Override
    public HyriPartyRank demoteMember(UUID uuid) {
        final HyriPartyRank rank = HyriPartyRank.getById(this.members.get(uuid).getId() - 1);
        this.members.put(uuid, rank);
        return rank;
    }

    @Override
    public IHyriParty removeMember(UUID uuid) {
        this.members.remove(uuid);
        return this;
    }

    @Override
    public boolean hasMember(UUID uuid) {
        return this.members.containsKey(uuid);
    }

    @Override
    public boolean isPrivate() {
        return this.privateParty;
    }

    @Override
    public IHyriParty setPrivate(boolean privateParty) {
        this.privateParty = privateParty;
        return this;
    }

    @Override
    public String getServer() {
        return this.server;
    }

    @Override
    public IHyriParty setServer(String server) {
        this.server = server;
        return this;
    }

    @Override
    public void sendMessage(String channel, String message, UUID sender, boolean force) {
        for (UUID uuid : this.members.keySet()) {
            HyriAPI.get().getChatChannelManager().sendMessageToPlayer(channel, message, uuid, sender, force);
        }
    }
}
