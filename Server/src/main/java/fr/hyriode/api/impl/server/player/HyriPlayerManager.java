package fr.hyriode.api.impl.server.player;

import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.api.impl.common.player.HyriCommonPlayerManager;
import fr.hyriode.api.impl.server.util.SpigotReflection;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 15:30
 */
public class HyriPlayerManager extends HyriCommonPlayerManager {

    public HyriPlayerManager(HydrionManager hydrionManager) {
        super(hydrionManager);
    }

    @Override
    public void kickPlayer(UUID uuid, String reason) {
        final Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            player.kickPlayer(reason);
        }
    }

    @Override
    public void sendTitle(UUID uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        final Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            final Consumer<Packet<?>> sendPacket = packet -> {
                final Object handle = SpigotReflection.getHandle(player);
                final Object playerConnection = SpigotReflection.invokeField(handle, "playerConnection");

                if (playerConnection != null) {
                    SpigotReflection.invokeMethod(playerConnection, "sendPacket", packet);
                }
            };

            sendPacket.accept(new PacketPlayOutTitle(fadeIn, stay, fadeOut));

            if (title != null) {
                sendPacket.accept(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatComponentText(title)));
            }
            if (subtitle != null) {
                sendPacket.accept(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, new ChatComponentText(subtitle)));
            }
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
