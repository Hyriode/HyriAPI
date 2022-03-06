package fr.hyriode.api.event;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 05/03/2022 at 18:04
 */
public enum HyriEventPriority {

    HIGHEST(4),
    HIGH(3),
    NORMAL(2),
    LOW(1),
    LOWEST(0);

    private final int weight;

    HyriEventPriority(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return this.weight;
    }

}
