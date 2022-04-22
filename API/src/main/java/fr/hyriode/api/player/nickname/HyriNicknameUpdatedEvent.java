package fr.hyriode.api.player.nickname;

import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.player.IHyriPlayer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 21:05
 */
public class HyriNicknameUpdatedEvent extends HyriEvent {

    private final IHyriPlayer player;
    private final IHyriNickname nickname;

    public HyriNicknameUpdatedEvent(IHyriPlayer player, IHyriNickname nickname) {
        this.player = player;
        this.nickname = nickname;
    }

    public IHyriPlayer getPlayer() {
        return this.player;
    }

    public IHyriNickname getNickname() {
        return this.nickname;
    }

}
