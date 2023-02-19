package fr.hyriode.api.rank;

import fr.hyriode.api.color.HyriChatColor;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 10:06
 */
public enum StaffRank implements IHyriRankType {

    HELPER("helper", 7, "Helper", HyriChatColor.DARK_PURPLE),
    DESIGNER("designer", 6, "Designer", HyriChatColor.GREEN),
    BUILDER("builder", 5, "Builder", HyriChatColor.GREEN),
    MODERATOR("moderator", 4, "Mod", HyriChatColor.DARK_AQUA),
    MODERATOR_PLUS("moderator+", 3, "Mod+", HyriChatColor.DARK_AQUA),
    DEVELOPER("developer", 2, "Dev", HyriChatColor.DARK_GREEN),
    MANAGER("manager", 1, "Manager", HyriChatColor.BLUE),
    ADMINISTRATOR("administrator", 0, "Admin", HyriChatColor.RED);

    private final int id;
    private final String name;
    private final int tabListPriority;
    private final String defaultPrefix;
    private final HyriChatColor defaultColor;

    StaffRank(String name, int tabListPriority, String defaultPrefix, HyriChatColor defaultColor) {
        this.id = this.ordinal();
        this.name = name;
        this.tabListPriority = tabListPriority;
        this.defaultPrefix = defaultColor.toString() + defaultPrefix;
        this.defaultColor = defaultColor;
    }

    public static StaffRank getById(int id) {
        for (StaffRank rankType : values()) {
            if (rankType.getId() == id) {
                return rankType;
            }
        }
        return null;
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
