package fr.hyriode.api.host;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 29/07/2022 at 19:58.<br>
 *
 * The data linked to a running host.
 */
public class HostData {

    /** The type of the host */
    private final HostType type;
    /** The owner of the host */
    private final UUID owner;

    /** The name of the host */
    private String name;
    /** Is the whitelist of the host active */
    private boolean whitelisted = true;
    /** Are spectators allowed on the server */
    private boolean spectatorsAllowed = true;

    /** The list of whitelisted players */
    private final Set<UUID> whitelistedPlayers = new HashSet<>();
    /** The list of secondary hosts */
    private final Set<UUID> secondaryHosts = new HashSet<>();

    /**
     * Main constructor of a {@link HostData}
     *
     * @param type The type of the host
     * @param owner The owner of the host
     * @param name The name of the host
     */
    public HostData(@NotNull HostType type, @NotNull UUID owner, @NotNull String name) {
        this.type = type;
        this.owner = owner;
        this.name = name;
    }

    /**
     * Get the type of the host
     *
     * @return A {@link HostType}
     */
    @NotNull
    public HostType getType() {
        return this.type;
    }

    /**
     * Get the owner of the host
     *
     * @return The {@link UUID} of the owner
     */
    public UUID getOwner() {
        return this.owner;
    }

    /**
     * Get the name of the host
     *
     * @return A name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of the host
     *
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Check whether the whitelist of the host is active or not.
     *
     * @return <code>true</code> if the whitelist is active
     */
    public boolean isWhitelisted() {
        return this.whitelisted;
    }

    /**
     * Define whether the whitelist of the host is active or not.
     *
     * @param whitelisted <code>true</code> to enable the whitelist
     */
    public void setWhitelisted(boolean whitelisted) {
        this.whitelisted = whitelisted;
    }

    /**
     * Check whether spectators are allowed on the host.
     *
     * @return <code>true</code> if spectators are allowed
     */
    public boolean isSpectatorsAllowed() {
        return this.spectatorsAllowed;
    }

    /**
     * Define whether spectators are allowed on the host.
     *
     * @param spectatorsAllowed <code>true</code> to allow spectators to connect
     */
    public void setSpectatorsAllowed(boolean spectatorsAllowed) {
        this.spectatorsAllowed = spectatorsAllowed;
    }

    /**
     * Get all the players whitelisted on the host.
     *
     * @return A list of player {@link UUID}
     */
    @NotNull
    public Set<UUID> getWhitelistedPlayers() {
        return this.whitelistedPlayers;
    }

    /**
     * Add a whitelisted player to the host.
     *
     * @param playerId The {@link UUID} of the player to add
     * @return <code>true</code> if the players was already added
     */
    public boolean addWhitelistedPlayer(@NotNull UUID playerId) {
        return this.whitelistedPlayers.add(playerId);
    }

    /**
     * Remove a whitelisted player from the host.
     *
     * @param playerId The {@link UUID} of the player to remove
     */
    public void removeWhitelistedPlayer(@NotNull UUID playerId) {
        this.whitelistedPlayers.remove(playerId);
    }

    /**
     * Get all the secondary owners of the host.
     *
     * @return A list of player {@link UUID}
     */
    @NotNull
    public Set<UUID> getSecondaryHosts() {
        return this.secondaryHosts;
    }

    /**
     * Add a secondary owner of the host.
     *
     * @param playerId The {@link UUID} of the player to add
     * @return <code>true</code> if the players was already added
     */
    public boolean addSecondaryHost(@NotNull UUID playerId) {
        return this.secondaryHosts.add(playerId);
    }

    /**
     * Remove a secondary owner of the host.
     *
     * @param playerId The {@link UUID} of the player to remove
     */
    public void removeSecondaryHost(@NotNull UUID playerId) {
        this.secondaryHosts.remove(playerId);
    }

}
