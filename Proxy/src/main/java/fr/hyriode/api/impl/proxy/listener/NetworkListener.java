package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.event.model.HyriMaintenanceEvent;
import fr.hyriode.api.impl.proxy.language.ProxyMessage;
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
                final IHyriPlayer account = IHyriPlayer.get(player.getUniqueId());

                if (account == null || !account.getRank().isStaff()) {
                    player.disconnect(ProxyMessage.MAINTENANCE.asFramedComponents(account, true));
                }
            }
        }
    }

}
