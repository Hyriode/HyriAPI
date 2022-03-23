package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.proxy.configuration.HyriAPIConfiguration;
import fr.hyriode.api.impl.proxy.util.SupportedProtocol;
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

    private final HyriAPIConfiguration configuration;

    @SuppressWarnings("deprecation")
    public HyriProxyListener(HyriAPIConfiguration configuration) {
        this.configuration = configuration;
        this.favicon = Favicon.create(this.configuration.getServerIcon());
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        final int protocolNumber = event.getConnection().getVersion();
        final IHyriNetwork network = HyriAPI.get().getNetwork();
        final int players = network.getPlayers();
        final int slots = network.getSlots();
        final ServerPing ping = new ServerPing();
        final ServerPing.PlayerInfo[] playerInfo = new ServerPing.PlayerInfo[]{};

        ping.setPlayers(new ServerPing.Players(slots, players, playerInfo));

        if (network.getMaintenance().isActive()) {
            ping.setVersion(new ServerPing.Protocol(ChatColor.DARK_AQUA + "Maintenance" + ChatColor.WHITE + " - " + ChatColor.GRAY + players + ChatColor.DARK_GRAY + "/" + ChatColor.GRAY + slots, -1));
        } else {
            ping.setVersion(new ServerPing.Protocol(ChatColor.DARK_RED + "Hyriode 1.8", SupportedProtocol.isSupported(protocolNumber) ? protocolNumber : SupportedProtocol.getDefault().getProtocolNumber()));
        }

        ping.setFavicon(this.favicon);
        ping.setDescriptionComponent(new TextComponent(network.getMotd()));

        event.setResponse(ping);
    }

}
