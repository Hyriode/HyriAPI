package fr.hyriode.api.impl.proxy.player;

import fr.hyriode.api.impl.common.player.HyriPlayerManager;
import net.md_5.bungee.BungeeTitle;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 15:30
 */
public class PHyriPlayerManager extends HyriPlayerManager {

    @Override
    public UUID getPlayerId(String name) {
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);

        if (player != null) {
            return player.getUniqueId();
        }
        return super.getPlayerId(name);
    }

    @Override
    public void kickPlayer(UUID uuid, String reason) {
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

        if (player != null) {
            player.disconnect(ComponentSerializer.parse(reason));
            return;
        }
        super.kickPlayer(uuid, reason);
    }

    @Override
    public void sendMessage(UUID uuid, String message, boolean component) {
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

        if (player != null) {
            if (component) {
                player.sendMessage(ComponentSerializer.parse(message));
            } else {
                player.sendMessage(TextComponent.fromLegacyText(message));
            }
            return;
        }
        super.sendMessage(uuid, message);
    }

    @Override
    public void sendTitle(UUID uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

        if (player != null) {
            sendTitleToPlayer(player, title, subtitle, fadeIn, stay, fadeOut);
            return;
        }
        super.sendTitle(uuid, title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public int getPing(UUID uuid) {
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

        if (player != null) {
            return player.getPing();
        }
        return -1;
    }

    public static void sendTitleToPlayer(ProxiedPlayer player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        final BungeeTitle bungeeTitle = new BungeeTitle();

        bungeeTitle.title(TextComponent.fromLegacyText(title));
        bungeeTitle.subTitle(TextComponent.fromLegacyText(subtitle));
        bungeeTitle.fadeIn(fadeIn);
        bungeeTitle.stay(stay);
        bungeeTitle.fadeOut(fadeOut);

        bungeeTitle.send(player);
    }

}
