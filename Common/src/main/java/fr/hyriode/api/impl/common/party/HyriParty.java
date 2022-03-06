package fr.hyriode.api.impl.common.party;

import fr.hyriode.api.party.IHyriParty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriParty implements IHyriParty {

    private String server;

    private UUID leader;

    private final List<UUID> members;

    private final UUID id;

    public HyriParty(UUID id, UUID leader, String server) {
        this.id = id;
        this.leader = leader;
        this.server = server;
        this.members = new ArrayList<>();
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
    public List<UUID> getMembers() {
        return this.members;
    }

    @Override
    public void addMember(UUID uuid) {
        this.members.add(uuid);
    }

    @Override
    public void removeMember(UUID uuid) {
        this.members.remove(uuid);
    }

    @Override
    public boolean hasMember(UUID uuid) {
        return this.members.contains(uuid);
    }

    @Override
    public String getServer() {
        return this.server;
    }

    @Override
    public void setServer(String server) {
        this.server = server;
    }

}
