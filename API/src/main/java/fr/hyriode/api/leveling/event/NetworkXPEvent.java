package fr.hyriode.api.leveling.event;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 11/02/2023 at 21:19
 */
public class NetworkXPEvent extends LevelingXPEvent {

    private final UUID playerId;

    public NetworkXPEvent(UUID playerId, String leveling, double oldExperience, double newExperience) {
        super(leveling, oldExperience, newExperience);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

}
