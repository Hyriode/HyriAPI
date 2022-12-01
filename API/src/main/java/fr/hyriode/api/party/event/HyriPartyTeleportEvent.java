package fr.hyriode.api.party.event;

import fr.hyriode.api.party.IHyriParty;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 23/11/2022 at 07:23
 */
public class HyriPartyTeleportEvent extends HyriPartyEvent {

    /** The player that teleported */
    private final UUID player;
    /** The target of the teleportation */
    private final UUID target;

    /**
     * Constructor of {@link HyriPartyEvent}
     *
     * @param party The {@link IHyriParty} object
     * @param player The player
     * @param target The target
     */
    public HyriPartyTeleportEvent(IHyriParty party, UUID player, UUID target) {
        super(party);
        this.player = player;
        this.target = target;
    }

    /**
     * Get the player that teleported
     *
     * @return A player {@link UUID}
     */
    public UUID getPlayer() {
        return this.player;
    }

    /**
     * Get the target of the teleportation
     *
     * @return A player {@link UUID}
     */
    public UUID getTarget() {
        return this.target;
    }

}
