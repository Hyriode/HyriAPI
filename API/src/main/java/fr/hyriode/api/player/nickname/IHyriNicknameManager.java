package fr.hyriode.api.player.nickname;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 21:58
 */
public interface IHyriNicknameManager {

    boolean isNicknameAvailable(String name);

    void addUsedNickname(String name, UUID player);

    void removeUsedNickname(String name);

    UUID getPlayerUsingNickname(String name);

}
