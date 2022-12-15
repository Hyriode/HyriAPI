package fr.hyriode.api.chat.channel;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.rank.type.HyriPlayerRankType;

import java.util.function.Predicate;

/**
 * All the channels where a player can send a message.
 */
public enum HyriChatChannel {

    /** The global channel accessible by all players */
    GLOBAL(false, player -> true),
    /** The channel only accessible by party members */
    PARTY(true, player -> {
        final IHyriPlayerSession session = IHyriPlayerSession.get(player.getUniqueId());

        return session != null && session.hasParty();
    }),
    /** The channel accessible by staff members */
    STAFF(true, player -> player.getRank().isStaff()),
    /** The channel accessible by {@link HyriPlayerRankType#PARTNER} */
    PARTNER(true, player -> player.getRank().is(HyriPlayerRankType.PARTNER) || player.getRank().isStaff());

    /** Defines whether messages sent on the channel are sent across the network or only on server. */
    private final boolean acrossNetwork;
    /** The {@link Predicate} which tests whether a player has access to the channel */
    private final Predicate<IHyriPlayer> accessibility;

    HyriChatChannel(boolean acrossNetwork, Predicate<IHyriPlayer> accessibility) {
        this.acrossNetwork = acrossNetwork;
        this.accessibility = accessibility;
    }

    /**
     * Check whether messages are sent across the network.
     *
     * @return <code>true</code> if yes
     */
    public boolean isAcrossNetwork() {
        return this.acrossNetwork;
    }

    /**
     * Check whether a player has access to the channel or not.
     *
     * @param player The player to test
     * @return <code>true</code> if he has access to the channel
     */
    public boolean hasAccess(IHyriPlayer player) {
        return this.accessibility.test(player);
    }

}
