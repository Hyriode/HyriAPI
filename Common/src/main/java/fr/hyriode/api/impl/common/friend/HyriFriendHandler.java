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
    public void addFriend(UUID friend) {
        if (!this.areFriends(friend)) {
            this.addFriend0(friend);

            HyriAPI.get().getFriendManager().createHandlerAsync(friend).whenComplete((friendHandler, throwable) -> {
                friendHandler.addFriend0(this.owner);

                HyriAPI.get().getFriendManager().updateFriends(friendHandler);
            });

            this.update();
        }
    }

    @Override
    public void addFriend0(UUID uuid) {
        this.friends.add(new HyriFriend(uuid));
    }

    @Override
    public void removeFriend(IHyriFriend friend) {
        this.friends.remove(friend);

        HyriAPI.get().getFriendManager().createHandlerAsync(friend.getUniqueId()).whenComplete((friendHandler, throwable) -> {
            friendHandler.removeFriend0(this.owner);

            HyriAPI.get().getFriendManager().updateFriends(friendHandler);
        });

        this.update();
    }

    @Override
    public void removeFriend(UUID uuid) {
        final IHyriFriend friend = this.getFriend(uuid);

        if (friend != null) {
            this.removeFriend(friend);
        }
    }

    @Override
    public void removeFriend0(UUID uuid) {
        final IHyriFriend friend = this.getFriend(uuid);

        if (friend != null) {
            this.friends.remove(friend);
        }
    }

    @Override
    public boolean areFriends(UUID uuid) {
        return this.getFriend(uuid) != null;
    }

    @Override
    public void update() {
        HyriAPI.get().getFriendManager().saveFriends(this);
    }

}
