package fr.hyriode.api.impl.common.friend;

import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.friend.IHyriFriendHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/04/2022 at 21:19
 */
public class HyriFriends {

    private final List<HyriFriend> friends;

    public HyriFriends(List<IHyriFriend> friends) {
        this.friends = new ArrayList<>();

        if (friends == null) {
            return;
        }

        for (IHyriFriend friend : friends) {
            this.friends.add((HyriFriend) friend);
        }
    }

    public HyriFriends(IHyriFriendHandler friendHandler) {
        this(friendHandler.getFriends());
    }

    public List<IHyriFriend> getFriends() {
        return new ArrayList<>(this.friends);
    }

}
