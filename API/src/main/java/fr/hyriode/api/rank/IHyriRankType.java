package fr.hyriode.api.rank;

import fr.hyriode.api.color.HyriChatColor;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 10:11
 */
public interface IHyriRankType {

    /**
     * Get the id of the rank type
     *
     * @return An id
     */
    int getId();

    /**
     * Get the name of the type
     *
     * @return A name
     */
    String getName();

    /**
     * Get the priority of the rank in queues
     *
     * @return A priority
     */
    int getPriority();

    /**
     * Get the priority in the tab list of the rank type
     *
     * @return A priority
     */
    int getTabListPriority();

    /**
     * Get the default prefix of the rank type
     *
     * @return A prefix
     */
    String getDefaultPrefix();

    /**
     * Get the default color of the rank type
     *
     * @return A {@link HyriChatColor}
     */
    HyriChatColor getDefaultColor();

    /**
     * Check if the type is using the separator
     *
     * @return <code>true</code> if yes
     */
    boolean withSeparator();

}
