package fr.hyriode.api.impl.server.player;

import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.player.HyriCommonPlayerManager;
import fr.hyriode.api.impl.server.util.SpigotReflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 15:30
 */
public class HyriPlayerManager extends HyriCommonPlayerManager {

    public HyriPlayerManager(HyriCommonImplementation implementation) {
        super(implementation);
    }

    @Override
    public void kickPlayer(UUID uuid, String reason) {
        final Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            player.kickPlayer(reason);
        }
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        final Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            player.sendMessage(message);
        }
    }

    @Override
    public int getPing(UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            final Object handle = SpigotReflection.getHandle(player);
            final Object ping = SpigotReflection.invokeField(handle, "ping");

            if (ping != null) {
                return (int) ping;
            }
        }
        return -1;
    }
}
