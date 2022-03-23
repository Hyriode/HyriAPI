package fr.hyriode.api.network;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/03/2022 at 09:10
 */
public interface IHyriMaintenance {

    /**
     * Enable the maintenance of the network
     *
     * @param trigger The player that triggered the maintenance
     * @param reason The reason of the maintenance
     * @return <code>true</code> if the maintenance has been successfully enabled
     */
    boolean enable(UUID trigger, String reason);

    /**
     * Disable the maintenance on the network
     *
     * @return <code>true</code> if the maintenance has been successfully disabled
     */
    boolean disable();

    /**
     * Check if the maintenance is active on the network
     *
     * @return <code>true</code> if the maintenance is active
     */
    boolean isActive();

    /**
     * Get the unique id of the player that triggered the maintenance.<br>
     * This will only work if the maintenance is active
     *
     * @return A {@link UUID}
     */
    UUID getTrigger();

    /**
     * Get the reason of the current maintenance
     *
     * @return The maintenance's reason
     */
    String getReason();

    /**
     * Set the reason of the maintenance.<br>
     * This will only work if the maintenance is active
     *
     * @param reason Nrw maintenance's reason
     */
    void setReason(String reason);

}
