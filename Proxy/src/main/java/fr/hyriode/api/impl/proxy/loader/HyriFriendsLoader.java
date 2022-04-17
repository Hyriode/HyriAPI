package fr.hyriode.api.impl.proxy.loader;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.friend.IHyriFriendManager;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.hydrion.client.module.FriendsModule;

import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 19:31
 */
public class HyriFriendsLoader {

    private final HydrionManager hydrionManager;
    private final IHyriFriendManager friendManager;
    private FriendsModule friendsModule;

    public HyriFriendsLoader(HydrionManager hydrionManager) {
        this.hydrionManager = hydrionManager;
        this.friendManager = HyriAPI.get().getFriendManager();

        if (this.hydrionManager.isEnabled()) {
            this.friendsModule = this.hydrionManager.getClient().getFriendsModule();
        }
    }

    public void loadFriends(UUID uuid) {
        this.friendManager.updateFriends(this.friendManager.createHandler(uuid));
    }

    public void unloadFriends(UUID uuid) {
        if (this.hydrionManager.isEnabled()) {
            final List<IHyriFriend> friends = this.friendManager.getFriends(uuid);

            this.friendManager.removeFriends(uuid);

            this.friendsModule.setFriends(uuid, HyriAPI.GSON.toJson(friends));
        }
    }

}
