package fr.hyriode.api.player.event;

import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.player.model.IHyriNickname;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 21:05
 */
public class NicknameUpdatedEvent extends HyriEvent {

    private final UUID playerId;
    private final IHyriNickname nickname;

    public NicknameUpdatedEvent(UUID playerId, IHyriNickname nickname) {
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
