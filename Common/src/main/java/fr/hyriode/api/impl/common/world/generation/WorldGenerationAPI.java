package fr.hyriode.api.impl.common.world.generation;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.world.IHyriWorld;
import fr.hyriode.api.world.generation.IWorldGenerationAPI;
import fr.hyriode.api.world.generation.WorldGenerationData;
import fr.hyriode.api.world.generation.WorldGenerationType;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 01/12/2022 at 20:16
 */
public class WorldGenerationAPI implements IWorldGenerationAPI {

    public static final String DATABASE = "world-generation";

    @Override
    public List<IHyriWorld> getWorlds(WorldGenerationType type) {
        return HyriAPI.get().getWorldManager().getWorlds(DATABASE, type.name());
    }

    @Override
    public void addWorld(UUID worldId, WorldGenerationType type, String name) {
        final IHyriWorld world = HyriAPI.get().getWorldManager().newWorld()
                .withDatabase(DATABASE)
                .withCategory(type.name())
                .withName(name)
                .build();

        HyriAPI.get().getWorldManager().saveWorld(world, worldId);
    }

    @Override
    public void removeWorld(WorldGenerationType type, String name) {
        HyriAPI.get().getWorldManager().deleteWorld(DATABASE, type.name(), name);
    }

    @Override
    public @Nullable WorldGenerationData getData(HyggServer server) {
        final String json = server.getData().get(DATA_KEY);

        return json == null ? null : HyriAPI.GSON.fromJson(json, WorldGenerationData.class);
    }

    @Override
    public @Nullable WorldGenerationData getData() {
        final String json = HyriAPI.get().getServer().getData().get(DATA_KEY);

        return json == null ? null : HyriAPI.GSON.fromJson(json, WorldGenerationData.class);
    }

}
