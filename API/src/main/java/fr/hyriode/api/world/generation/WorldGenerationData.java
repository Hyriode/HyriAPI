package fr.hyriode.api.world.generation;

/**
 * Created by AstFaster
 * on 26/10/2022 at 09:55
 */
public class WorldGenerationData {

    private final WorldGenerationType type;
    private final int worlds;

    public WorldGenerationData(WorldGenerationType type, int worlds) {
        this.type = type;
        this.worlds = worlds;
    }

    public WorldGenerationType getType() {
        return this.type;
    }

    public int getWorlds() {
        return this.worlds;
    }

}
