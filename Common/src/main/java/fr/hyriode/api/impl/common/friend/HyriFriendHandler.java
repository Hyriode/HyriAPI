package fr.hyriode.api.impl.common.friend;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.friend.IHyriFriendHandler;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/03/2022 at 15:36
 */
public class HyriFriendHandler implements IHyriFriendHandler {

    private final UUID owner;
    private final List<IHyriFriend> friends;

    public HyriFriendHandler(UUID owner, List<IHyriFriend> friends) {
        this.owner = owner;
        this.friends = friends;
    }

    @Override
    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public IHyriFriend getFriend(UUID uuid) {
        for (IHyriFriend friend : this.friends) {
            if (friend.getUniqueId().equals(uuid)) {
                return friend;
            }
        }
        return null;
    }

    @Override
    public List<IHyriFriend> getFriends() {
        return this.friends;
    }

    @Override
    public List<UUID> getFriendsIds() {
        return this.friends.stream().map(IHyriFriend::getUniqueId).collect(Collectors.toList());
    }

    @Override
    public void addFriend(IHyriFriend friend) {
        if (!this.areFriends(friend.getUniqueId())) {
            this.friends.add(friend);

            this.update();
        }
    }

    @Override
    public void addFriend(UUID uuid) {
        this.addFriend(new HyriFriend(uuid));
    }

    @Override
    public void removeFriend(IHyriFriend friend) {
        this.friends.remove(friend);

        this.update();
    }

    @Override
    public void removeFriend(UUID uuid) {
        if (this.areFriends(uuid)) {
            this.friends.remove(this.getFriend(uuid));

            this.update();
        }
    }

    @Override
    public boolean areFriends(UUID uuid) {
        return this.getFriend(uuid) != null;
    }

    @Override
    public void update() {
        HyriAPI.get().getFriendManager().updateFriends(this);
    }

}
