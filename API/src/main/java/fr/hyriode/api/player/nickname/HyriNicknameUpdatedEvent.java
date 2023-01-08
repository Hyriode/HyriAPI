package fr.hyriode.api.player.nickname;

import fr.hyriode.api.event.HyriEvent;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 21:05
 */
public class HyriNicknameUpdatedEvent extends HyriEvent {

    private final UUID playerId;
    private final IHyriNickname nickname;

    public HyriNicknameUpdatedEvent(UUID playerId, IHyriNickname nickname) {
        this.playerId = playerId;
        this.nickname = nickname;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public IHyriNickname getNickname() {
        return this.nickname;
    }

}
