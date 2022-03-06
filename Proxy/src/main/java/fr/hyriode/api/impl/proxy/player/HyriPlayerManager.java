package fr.hyriode.api.impl.proxy.player;

import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.player.HyriCommonPlayerManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

        if (player != null) {
            player.disconnect(TextComponent.fromLegacyText(reason));
        }
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

        if (player != null) {
            player.sendMessage(TextComponent.fromLegacyText(message));
        }
    }

    @Override
    public int getPing(UUID uuid) {
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

        if (player != null) {
            return player.getPing();
        }
        return -1;
    }

}
