package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.proxy.config.HyriAPIConfig;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.network.IHyriNetworkManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/02/2022 at 14:46
 */
public class ProxyListener implements Listener {

    private final Favicon favicon;

    @SuppressWarnings("deprecation")
    public ProxyListener(HyriAPIConfig configuration) {
        this.favicon = Favicon.create(configuration.getServerIcon());
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        final int protocolNumber = event.getConnection().getVersion();
        final IHyriNetworkManager networkManager = HyriAPI.get().getNetworkManager();
        final IHyriNetwork network = networkManager.getNetwork();
        final int players = networkManager.getPlayerCounter().getPlayers();
        final int slots = network.getSlots();
        final ServerPing ping = new ServerPing();
        final ServerPing.PlayerInfo[] playerInfo = new ServerPing.PlayerInfo[]{};

        ping.setPlayers(new ServerPing.Players(slots, players, playerInfo));

        if (network.getMaintenance().isActive()) {
            ping.setVersion(new ServerPing.Protocol(ChatColor.DARK_AQUA + "Maintenance" + ChatColor.WHITE + " - " + ChatColor.GRAY + players + ChatColor.DARK_GRAY + "/" + ChatColor.GRAY + slots, -1));
        } else {
            ping.setVersion(new ServerPing.Protocol(ChatColor.DARK_RED + "Hyriode 1.8-1.19", Math.max(protocolNumber, 47)));
        }

        ping.setFavicon(this.favicon);

        if (network.getMotd() != null) {
            final String[] motd = network.getMotd().split("%new_line%");
            final String firstLine = motd[0];

            String secondLine = "";
            if (motd.length > 1) {
                secondLine = motd[1];
            }

            ping.setDescriptionComponent(new TextComponent(TextComponent.fromLegacyText(firstLine + "\n" + secondLine)));
        }

        event.setResponse(ping);
    }

}
