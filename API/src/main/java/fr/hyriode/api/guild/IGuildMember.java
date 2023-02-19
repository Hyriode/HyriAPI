package fr.hyriode.api.guild;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 12/02/2023 at 23:52.<br>
 *
 * This class represents what a member of a {@linkplain IHyriGuild guild} is.
 */
public interface IGuildMember {

    /**
     * Get the unique id of the guild's member
     *
     * @return A {@link UUID}
     */
    @NotNull UUID getUniqueId();

    /**
     * Get the name of the rank that the member has in the guild
     *
     * @return A rank name. E.g. Leader
     */
    @NotNull String getRank();

    /**
     * Set the rank of the member in the guild
     *
     * @param rankName A rank name. E.g. Officer
     */
    void setRank(@NotNull String rankName);

    /**
     * Get the total amount of experience earned by the player for his guild
     *
     * @return An experience amount
     */
    double getEarnedExperience();

    /**
     * Add an amount of experience earned by the player for his guild
     *
     * @param experience An experience amount
     */
    void addEarnedExperience(double experience);

    /**
     * Get the total amount of Hyris deposited by the player for his guild
     *
     * @return A Hyris amount
     */
    long getDepositedHyris();

    /**
     * Add an amount of Hyris deposited by the player for his guild
     *
     * @param hyris A Hyris amount
     */
    void addDepositedHyris(long hyris);

    /**
     * Get the timestamp when the player joined the guild
     *
     * @return A timestamp (in milliseconds)
     */
    long getJoinedDate();

}
