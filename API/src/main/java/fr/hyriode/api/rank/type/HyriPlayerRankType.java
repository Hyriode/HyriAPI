package fr.hyriode.api.rank.type;

import fr.hyriode.api.color.HyriChatColor;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 10:06
 */
public enum HyriPlayerRankType implements IHyriRankType {

    /** Default ranks */
    PLAYER(0, "", HyriChatColor.GRAY, false),
    VIP(1, "VIP", HyriChatColor.YELLOW),
    VIP_PLUS(2, "VIP+", HyriChatColor.GREEN),
    EPIC(3, "Epic", HyriChatColor.AQUA),

    /** Content creator */
    PARTNER(4, "Partner", HyriChatColor.GOLD);

    private final int id;
    private final String defaultPrefix;
    private final HyriChatColor defaultColor;
    private final boolean separator;

    HyriPlayerRankType(int id, String defaultPrefix, HyriChatColor defaultColor, boolean separator) {
        this.id = id;
        this.defaultPrefix = defaultColor.getName() + defaultPrefix;
        this.defaultColor = defaultColor;
        this.separator = separator;
    }

    HyriPlayerRankType(int id, String defaultPrefix, HyriChatColor defaultColor) {
        this(id, defaultPrefix, defaultColor, true);
    }

    @Override
    public int getId() {
        return this.id;
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
