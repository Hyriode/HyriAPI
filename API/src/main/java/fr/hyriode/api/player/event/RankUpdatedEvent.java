package fr.hyriode.api.player.event;

import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.IHyriRank;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 04/07/2022 at 16:49
 */
public class RankUpdatedEvent extends HyriEvent {

    private final UUID playerId;

    public RankUpdatedEvent(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public IHyriPlayer getAccount() {
        return IHyriPlayer.get(this.playerId);
    }

    public IHyriRank getRank() {
        return this.getAccount().getRank();
    }

}
