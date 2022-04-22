package fr.hyriode.api.impl.server;

import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.server.HyriCServerManager;
import fr.hyriode.api.impl.server.join.HyriJoinManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 21:53
 */
public class HyriServerManager extends HyriCServerManager {

    private final HyriJoinManager joinManager;

    public HyriServerManager(JavaPlugin plugin, HyriCommonImplementation implementation) {
        super(implementation);
        this.joinManager = new HyriJoinManager(plugin);
    }

    @Override
    public HyriJoinManager getJoinManager() {
        return this.joinManager;
    }

}
