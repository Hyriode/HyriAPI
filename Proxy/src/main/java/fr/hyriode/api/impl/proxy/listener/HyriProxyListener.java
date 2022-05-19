package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.proxy.configuration.HyriAPIConfig;
import fr.hyriode.api.network.IHyriNetwork;
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
public class HyriProxyListener implements Listener {

    private final Favicon favicon;

    @SuppressWarnings("deprecation")
    public HyriProxyListener(HyriAPIConfig configuration) {
        this.favicon = Favicon.create(configuration.getServerIcon());
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        final int protocolNumber = event.getConnection().getVersion();
        final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();
        final int players = network.getPlayerCount().getPlayers();
        final int slots = network.getSlots();
        final ServerPing ping = new ServerPing();
        final ServerPing.PlayerInfo[] playerInfo = new ServerPing.PlayerInfo[]{};

        ping.setPlayers(new ServerPing.Players(slots, players, playerInfo));

        if (network.getMaintenance().isActive()) {
            ping.setVersion(new ServerPing.Protocol(ChatColor.DARK_AQUA + "Maintenance" + ChatColor.WHITE + " - " + ChatColor.GRAY + players + ChatColor.DARK_GRAY + "/" + ChatColor.GRAY + slots, -1));
        } else {
            ping.setVersion(new ServerPing.Protocol(ChatColor.DARK_RED + "Hyriode 1.8-1.18", protocolNumber));
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
