package fr.hyriode.api.rank;

import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 04/07/2022 at 16:49
 */
public class HyriRankUpdatedEvent extends HyriEvent {

    private final UUID playerId;

    public HyriRankUpdatedEvent(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public IHyriPlayer getAccount() {
        return IHyriPlayer.get(this.playerId);
    }

    public HyriRank getRank() {
        return this.getAccount().getRank();
    }

}
