package fr.hyriode.api.impl.server.player;

import fr.hyriode.api.impl.common.player.CHyriPlayerManager;
import fr.hyriode.api.impl.server.util.SpigotReflection;
import net.md_5.bungee.chat.ComponentSerializer;
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
public class HyriPlayerManager extends CHyriPlayerManager {

    @Override
    public UUID getPlayerId(String name) {
        final Player player = Bukkit.getPlayer(name);

        if (player != null) {
            return player.getUniqueId();
        }
        return super.getPlayerId(name);
    }

    @Override
    public void sendMessage(UUID uuid, String message, boolean component) {
        final Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            if (component) {
                player.spigot().sendMessage(ComponentSerializer.parse(message));
            } else {
                player.sendMessage(message);
            }
            return;
        }
        super.sendMessage(uuid, message);
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
            return;
        }
        super.sendTitle(uuid, title, subtitle, fadeIn, stay, fadeOut);
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
