package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.impl.proxy.loader.HyriFriendsLoader;
import fr.hyriode.api.impl.proxy.loader.HyriPlayerLoader;
import fr.hyriode.api.network.IHyriMaintenance;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/02/2022 at 14:41
 */
public class HyriJoinListener implements Listener {

    private static final String WARNING = "\u26A0";

    private final HyriPlayerLoader playerLoader;
    private final HyriFriendsLoader friendsLoader;

    private final HyggdrasilManager hyggdrasilManager;

    public HyriJoinListener(HyriCommonImplementation api) {
        this.hyggdrasilManager = api.getHyggdrasilManager();
        this.playerLoader = new HyriPlayerLoader(api.getHydrionManager());
        this.friendsLoader = new HyriFriendsLoader(api.getHydrionManager());
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        try {
            final PendingConnection connection = event.getConnection();
            final UUID uuid = connection.getUniqueId();
            final String name = connection.getName();

            this.playerLoader.loadPlayerAccount(uuid, name);
            this.friendsLoader.loadFriends(uuid);
        } catch (Exception e) {
            e.printStackTrace();

            event.setCancelled(true);
            event.setCancelReason(TextComponent.fromLegacyText(ChatColor.RED + "An error occurred while loading your profile!"));
        }
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();
            final IHyriMaintenance maintenance = network.getMaintenance();

            event.setCancelled(true);

            if (maintenance.isActive()) {
                player.disconnect(this.buildMessage("Une maintenance est actuellement en cours. Raison: " + maintenance.getReason()));
            } else if (network.getPlayerCount().getPlayers() >= network.getSlots()) {
                player.disconnect(this.buildMessage("Le serveur est plein !"));
            } else {
                final IHyriServerManager serverManager = HyriAPI.get().getServerManager();
                final HyggServer lobby = serverManager.getLobby();

                if (lobby == null) {
                    player.disconnect(this.buildMessage("Aucun lobby n'est actuellement démarré !"));
                } else {
                    event.setTarget(ProxyServer.getInstance().getServerInfo(lobby.getName()));
                    event.setCancelled(false);

                    network.getPlayerCount().addPlayers(1);
                    network.update();

                    this.hyggdrasilManager.sendData();
                }
            }
        }
    }

    private TextComponent buildMessage(String reason) {
        return new TextComponent(new ComponentBuilder("◆ Hyriode ◆").color(ChatColor.DARK_AQUA)
                .append("\n")
                .append(WARNING + " " + reason + " " + WARNING).color(ChatColor.RED)
                .create());
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        if (player.isConnected()) {
            System.out.println("Connected");
            final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();
            final UUID uuid = player.getUniqueId();

            this.playerLoader.unloadPlayerAccount(uuid);
            this.friendsLoader.unloadFriends(uuid);

            network.getPlayerCount().removePlayers(1);
            network.update();

            this.hyggdrasilManager.sendData();
        }
    }

}
