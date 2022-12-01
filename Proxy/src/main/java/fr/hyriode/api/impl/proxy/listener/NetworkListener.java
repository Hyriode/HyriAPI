package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.event.model.HyriMaintenanceEvent;
import fr.hyriode.api.impl.proxy.util.MessageUtil;
import fr.hyriode.api.player.IHyriPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 20/04/2022 at 10:10
 */
public class NetworkListener {

    @HyriEventHandler
    public void onMaintenance(HyriMaintenanceEvent event) {
        if (event.getAction() == HyriMaintenanceEvent.Action.ENABLED) {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId());

                if (!account.getRank().isStaff()) {
                    player.disconnect(MessageUtil.createMaintenanceMessage(event.getMaintenance()));
                }
            }
        }
    }

}
