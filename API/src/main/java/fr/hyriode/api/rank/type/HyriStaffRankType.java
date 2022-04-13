package fr.hyriode.api.rank.type;

import fr.hyriode.api.color.HyriChatColor;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 10:06
 */
public enum HyriStaffRankType implements IHyriRankType {

    HELPER(0, "Helper", HyriChatColor.DARK_PURPLE),
    DESIGNER(1, "Designer", HyriChatColor.GREEN),
    BUILDER(2, "Builder", HyriChatColor.GREEN),
    MODERATOR(3, "Mod", HyriChatColor.DARK_AQUA),
    DEVELOPER(4, "Dev", HyriChatColor.DARK_GREEN),
    MANAGER(5, "Manager", HyriChatColor.BLUE),
    ADMINISTRATOR(6, "Admin", HyriChatColor.RED);

    private final int id;
    private final String defaultPrefix;
    private final HyriChatColor defaultColor;

    HyriStaffRankType(int id, String defaultPrefix, HyriChatColor defaultColor) {
        this.id = id;
        this.defaultPrefix = defaultColor.getName() + defaultPrefix;
        this.defaultColor = defaultColor;
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
        return true;
    }

}
