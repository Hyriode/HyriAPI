package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/02/2022 at 14:46
 */
public class HyriProxyListener implements Listener {

    private static final String ENCODED_FAVICON = "data:image/png;base64,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";

    private final Favicon favicon;

    @SuppressWarnings("deprecation")
    public HyriProxyListener() {
        this.favicon = Favicon.create(ENCODED_FAVICON);
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        final ServerPing ping = new ServerPing();
        final ServerPing.PlayerInfo[] playerInfo = new ServerPing.PlayerInfo[]{};
        final BaseComponent[] description = new ComponentBuilder("Hyriode").color(ChatColor.DARK_AQUA)
                .append(" ⬥ ").color(ChatColor.WHITE)
                .append("discord.hyriode.fr").color(ChatColor.AQUA)
                .append(" ┃ ").color(ChatColor.WHITE)
                .append("2022").color(ChatColor.DARK_AQUA)
                .create();

        ping.setPlayers(new ServerPing.Players(250, HyriAPI.get().getProxy().getPlayers(), playerInfo));
        ping.setVersion(new ServerPing.Protocol("1.8", 47));
        ping.setFavicon(this.favicon);
        ping.setDescriptionComponent(new TextComponent(description));

        event.setResponse(ping);
    }

}
