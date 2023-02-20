package fr.hyriode.api.world.generation;

import fr.hyriode.api.world.IHyriWorld;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 01/12/2022 at 20:11
 */
public interface IWorldGenerationAPI {

    /** The key of the {@link WorldGenerationData} object. */
    String DATA_KEY = "world-generation";
    /** The type of the servers which generates new worlds. */
    String SERVERS_TYPE = "gen";

    /**
     * Get all the available worlds for a given type
     *
     * @param type The type of worlds to search
     * @return A list of world
     */
    List<IHyriWorld> getWorlds(WorldGenerationType type);

    /**
     * Store a new world in database
     *
     * @param worldId The {@link UUID} of the world to save
     * @param type The type of the world
     * @param name The name of the world
     */
    void addWorld(UUID worldId, WorldGenerationType type, String name);

    /**
     * Remove a world from available worlds
     *
     * @param type The type of the world to remove
     * @param name The name of the world to remove
     */
    void removeWorld(WorldGenerationType type, String name);

    /**
     * Get the world generation data from a given server
     *
     * @param server The server
     * @return A {@link WorldGenerationData}; or <code>null</code> if the server is not generating worlds
     */
    @Nullable WorldGenerationData getData(HyggServer server);

    /**
     * Get the world generation data from the current server
     *
     * @return  A {@link WorldGenerationData}; or <code>null</code> if the server is not generating worlds
     */
    @Nullable WorldGenerationData getData();

}
