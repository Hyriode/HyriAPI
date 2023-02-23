package fr.hyriode.api.impl.server.world;

import fr.hyriode.api.impl.common.world.HyriWorldManager;
import fr.hyriode.api.world.IHyriWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 20/02/2023 at 09:47
 */
public class SHyriWorldManager extends HyriWorldManager {

    public SHyriWorldManager() {
        super(mongoDB);
    }

    @Override
    public void saveWorld(@NotNull IHyriWorld world, @NotNull UUID worldId) {
        final World bukkitWorld = Bukkit.getWorld(worldId);

        if (bukkitWorld != null) {
            bukkitWorld.save();

            this.saveWorld(world, bukkitWorld.getWorldFolder());
        }
    }

}
