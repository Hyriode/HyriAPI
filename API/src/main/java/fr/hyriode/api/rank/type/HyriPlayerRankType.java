package fr.hyriode.api.rank.type;

import fr.hyriode.api.color.HyriChatColor;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 10:06
 */
public enum HyriPlayerRankType implements IHyriRankType {

    /** Default ranks */
    PLAYER(0, "player", 10, 12, "", HyriChatColor.GRAY, false),
    VIP(1, "vip", 9, 11, "VIP", HyriChatColor.YELLOW),
    VIP_PLUS(2, "vip+", 8, 10, "VIP+", HyriChatColor.GREEN),
    EPIC(3, "epic", 7, 9, "Epic", HyriChatColor.AQUA),

    /** Content creator */
    PARTNER(4, "partner", 5, 7, "Partner", HyriChatColor.GOLD);

    private final int id;
    private final String name;
    private final int priority;
    private final int tabListPriority;
    private final String defaultPrefix;
    private final HyriChatColor defaultColor;
    private final boolean separator;

    HyriPlayerRankType(int id, String name, int priority, int tabListPriority, String defaultPrefix, HyriChatColor defaultColor, boolean separator) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.tabListPriority = tabListPriority;
        this.defaultPrefix = defaultColor.toString() + defaultPrefix;
        this.defaultColor = defaultColor;
        this.separator = separator;
    }

    HyriPlayerRankType(int id, String name, int priority, int tabListPriority, String defaultPrefix, HyriChatColor defaultColor) {
        this(id, name, priority, tabListPriority, defaultPrefix, defaultColor, true);
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

}
