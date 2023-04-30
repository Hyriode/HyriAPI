package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.impl.proxy.language.ProxyMessage;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.proxy.event.ProxyRestartingEvent;
import fr.hyriode.api.sound.HyriSound;
import fr.hyriode.api.sound.HyriSoundPacket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Created by AstFaster
 * on 30/04/2023 at 19:40
 */
public class RestartListener {

    @HyriEventHandler
    public void onRestart(ProxyRestartingEvent event) {
        if (event.getProxy().equals(HyriAPI.get().getProxy().getName())) {
            final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
            final int count = event.getCount();

            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                player.sendMessage(ProxyMessage.PROXY_RESTARTING_MESSAGE.asComponents(player, input -> input.replace("%count%", String.valueOf(count))));
                playerManager.sendTitle(player.getUniqueId(),
                        ProxyMessage.PROXY_RESTARTING_TITLE.asString(player),
                        ProxyMessage.PROXY_RESTARTING_SUBTITLE.asString(player).replace("%count%", String.valueOf(count)),
                        0, 5 * 20, 0);
                HyriSoundPacket.send(player.getUniqueId(), HyriSound.ORB_PICKUP, 2.0f, 5.0f);
            }
        }
    }

}
