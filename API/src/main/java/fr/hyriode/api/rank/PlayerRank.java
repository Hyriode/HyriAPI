package fr.hyriode.api.rank;

import fr.hyriode.api.color.HyriChatColor;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 10:06
 */
public enum PlayerRank implements IHyriRankType {

    /** Default ranks */
    PLAYER("player", 10, 13, "", HyriChatColor.GRAY, false),
    VIP("vip", 9, 12, "VIP", HyriChatColor.YELLOW),
    VIP_PLUS("vip+", 8, 11, "VIP+", HyriChatColor.GREEN),
    EPIC("epic", 7, 10, "Epic", HyriChatColor.AQUA),

    /** Content creator */
    PARTNER("partner", 5, 8, "Partner", HyriChatColor.GOLD);

    private final int id;
    private final String name;
    private final int priority;
    private final int tabListPriority;
    private final String defaultPrefix;
    private final HyriChatColor defaultColor;
    private final boolean separator;

    PlayerRank(String name, int priority, int tabListPriority, String defaultPrefix, HyriChatColor defaultColor, boolean separator) {
        this.id = this.ordinal();
        this.name = name;
        this.priority = priority;
        this.tabListPriority = tabListPriority;
        this.defaultPrefix = defaultColor.toString() + defaultPrefix;
        this.defaultColor = defaultColor;
        this.separator = separator;
    }

    PlayerRank(String name, int priority, int tabListPriority, String defaultPrefix, HyriChatColor defaultColor) {
        this(name, priority, tabListPriority, defaultPrefix, defaultColor, true);
    }

    public static PlayerRank getById(int id) {
        for (PlayerRank rankType : values()) {
            if (rankType.getId() == id) {
                return rankType;
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public int getTabListPriority() {
        return this.tabListPriority;
    }

    @Override
    public String getDefaultPrefix() {
        return this.defaultPrefix;
    }

    @Override
    public HyriChatColor getDefaultColor() {
        return this.defaultColor;
    }

    @Override
    public boolean withSeparator() {
        return this.separator;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
