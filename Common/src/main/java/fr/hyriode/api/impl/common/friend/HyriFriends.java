package fr.hyriode.api.impl.common.friend;

import fr.hyriode.api.friend.IHyriFriend;

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

        for (IHyriFriend friend : friends) {
            this.friends.add((HyriFriend) friend);
        }
    }

    public List<IHyriFriend> getFriends() {
        return new ArrayList<>(this.friends);
    }

}
