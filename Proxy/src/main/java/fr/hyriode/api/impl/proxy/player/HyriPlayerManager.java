package fr.hyriode.api.impl.proxy.player;

import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.api.impl.common.player.HyriCPlayerManager;
import fr.hyriode.api.impl.proxy.util.MessageUtil;
import net.md_5.bungee.BungeeTitle;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 15:30
 */
public class HyriPlayerManager extends HyriCPlayerManager {

    public HyriPlayerManager(HydrionManager hydrionManager) {
        super(hydrionManager);
    }

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
            player.disconnect(MessageUtil.deserializeComponent(reason));
            return;
        }
        super.kickPlayer(uuid, reason);
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

        if (player != null) {
            player.sendMessage(TextComponent.fromLegacyText(message));
            return;
        }
        super.sendMessage(uuid, message);
    }


    @Override
    public void sendComponent(UUID uuid, String component) {
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

        if (player != null) {
            player.sendMessage(MessageUtil.deserializeComponent(component));
            return;
        }
        super.sendComponent(uuid, component);
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
