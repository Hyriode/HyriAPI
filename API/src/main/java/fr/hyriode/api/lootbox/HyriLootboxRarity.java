package fr.hyriode.api.lootbox;

/**
 * Created by AstFaster
 * on 03/10/2022 at 19:31
 */
public enum HyriLootboxRarity {

    ONE_STAR(1),
    TWO_STARS(2),
    THREE_STARS(3),
    FOUR_STARS(4),
    FIVE_STARS(5);

    private final int id;

    HyriLootboxRarity(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

}
