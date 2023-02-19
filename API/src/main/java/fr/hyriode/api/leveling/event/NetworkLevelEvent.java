package fr.hyriode.api.leveling.event;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 11/02/2023 at 21:18
 */
public class NetworkLevelEvent extends LevelingLevelEvent {

    private final UUID playerId;

    public NetworkLevelEvent(UUID playerId, String leveling, int oldLevel, int newLevel) {
        super(leveling, oldLevel, newLevel);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

}
