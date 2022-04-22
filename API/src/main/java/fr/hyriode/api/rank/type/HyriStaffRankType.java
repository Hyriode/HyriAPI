package fr.hyriode.api.rank.type;

import fr.hyriode.api.color.HyriChatColor;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 10:06
 */
public enum HyriStaffRankType implements IHyriRankType {

    HELPER(0, "helper", 6, "Helper", HyriChatColor.DARK_PURPLE),
    DESIGNER(1, "designer", 5, "Designer", HyriChatColor.GREEN),
    BUILDER(2, "builder", 4, "Builder", HyriChatColor.GREEN),
    MODERATOR(3, "moderator", 3, "Mod", HyriChatColor.DARK_AQUA),
    DEVELOPER(4, "developer", 2, "Dev", HyriChatColor.DARK_GREEN),
    MANAGER(5, "manager", 1, "Manager", HyriChatColor.BLUE),
    ADMINISTRATOR(6, "administrator", 0, "Admin", HyriChatColor.RED);

    private final int id;
    private final String name;
    private final int tabListPriority;
    private final String defaultPrefix;
    private final HyriChatColor defaultColor;

    HyriStaffRankType(int id, String name, int tabListPriority, String defaultPrefix, HyriChatColor defaultColor) {
        this.id = id;
        this.name = name;
        this.tabListPriority = tabListPriority;
        this.defaultPrefix = defaultColor.toString() + defaultPrefix;
        this.defaultColor = defaultColor;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getPriority() {
        return 1;
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
        return true;
    }

}
