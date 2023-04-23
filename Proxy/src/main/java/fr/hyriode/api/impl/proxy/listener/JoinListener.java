package fr.hyriode.api.impl.proxy.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.proxy.language.ProxyMessage;
import fr.hyriode.api.impl.proxy.player.PlayerLoader;
import fr.hyriode.api.network.IHyriMaintenance;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.player.event.PlayerJoinNetworkEvent;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.api.server.reconnection.IHyriReconnectionData;
import fr.hyriode.hyggdrasil.api.limbo.HyggLimbo;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/02/2022 at 14:41
 */
public class JoinListener implements Listener {

    private final Map<String, AtomicInteger> ipLimits = new ConcurrentHashMap<>();
    private final Set<UUID> reconnections = ConcurrentHashMap.newKeySet();

    private final PlayerLoader playerLoader;

    public JoinListener() {
        this.playerLoader = new PlayerLoader();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPreLogin(PreLoginEvent event) {
        if (event.isCancelled()) {
            return;
        }

        try {
            final PendingConnection connection = event.getConnection();
            final String name = connection.getName();
            final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();
            final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
            final IHyriMaintenance maintenance = network.getMaintenance();
            final boolean whitelisted = playerManager.getWhitelistManager().isWhitelisted(name);

            UUID playerId = playerManager.getPlayerId(name);
            PlayerLoader.MojangProfile mojangProfile = null;

            if (playerId == null) {
                if (maintenance.isActive() && !whitelisted) {
                    event.setCancelled(true);
                    event.setCancelReason(ProxyMessage.MAINTENANCE.asFramedComponents(null, true));
                    return;
                } else if (network.getPlayerCounter().getPlayers() >= network.getSlots()) {
                    event.setCancelled(true);
                    event.setCancelReason(ProxyMessage.SERVER_FULL.asFramedComponents(null, false));
                    return;
                }

                mojangProfile = this.playerLoader.fetchMojangProfile(name);
                playerId = mojangProfile.getPlayerId();
            }

            IHyriPlayer account = IHyriPlayer.get(playerId);

            if (account != null) {
                final boolean bypass = account.getRank().isStaff() || whitelisted;

                if (maintenance.isActive() && !bypass) {
                    event.setCancelled(true);
                    event.setCancelReason(ProxyMessage.MAINTENANCE.asFramedComponents(account, true));
                    return;
                } else if (network.getPlayerCounter().getPlayers() >= network.getSlots() && !bypass && !account.getRank().isSuperior(PlayerRank.VIP_PLUS)) {
                    event.setCancelled(true);
                    event.setCancelReason(ProxyMessage.SERVER_FULL.asFramedComponents(account, false));
                    return;
                }

                if (this.playerLoader.isOnline(playerId)) { // Check if the player is not online
                    event.setCancelled(true);
                    event.setCancelReason(ProxyMessage.ALREADY_ONLINE.asFramedComponents(account, true));
                    return;
                } else if (!account.getAuth().isPremium()) { // Player is not premium but his name might have been taken
                    if (mojangProfile == null) {
                        mojangProfile = this.playerLoader.fetchMojangProfile(name);
                    }

                    if (mojangProfile.isPremium()) { // Mojang tells the queried name is now owned by a premium user. The crack player must transfer his account. And we will create a new account for the premium player.
                        HyriAPI.get().getPlayerManager().savePlayerId(name, mojangProfile.getPlayerId());

                        event.setCancelled(true);
                        event.setCancelReason(ProxyMessage.PROFILE_TAKEN.asFramedComponents(account, true));
                        return;
                    }
                }
            }

            if (account == null) { // Player doesn't exist so a new account need to be created
                if (maintenance.isActive() && !whitelisted) {
                    event.setCancelled(true);
                    event.setCancelReason(ProxyMessage.MAINTENANCE.asFramedComponents(null, true));
                    return;
                } else if (network.getPlayerCounter().getPlayers() >= network.getSlots() && !whitelisted) {
                    event.setCancelled(true);
                    event.setCancelReason(ProxyMessage.SERVER_FULL.asFramedComponents(null, false));
                    return;
                }

                if (!this.playerLoader.isNameValid(name)) {
                    event.setCancelled(true);
                    event.setCancelReason(ProxyMessage.INVALID_NAME.asFramedComponents(null, true));
                    return;
                }

                if (mojangProfile == null) {
                    mojangProfile = this.playerLoader.fetchMojangProfile(name); // Queries Mojang to check if the player is premium
                    playerId = mojangProfile.getPlayerId();
                }

                if (mojangProfile.isPremium()) { // Only create the account if the player is premium! (for crack users it will be after being registered)
                    account = playerManager.createPlayer(true, playerId, name);
                }
            }

            final String ip = ((InetSocketAddress) connection.getSocketAddress()).getAddress().getHostAddress();
            final AtomicInteger ipLimit = this.ipLimits.getOrDefault(ip, new AtomicInteger(0));

            if (ipLimit.incrementAndGet() > 3) {
                this.ipLimits.remove(ip);

                event.setCancelled(true);
                event.setCancelReason(ProxyMessage.IP_LIMIT.asFramedComponents(null, true));
                return;
            }

            this.ipLimits.put(ip, ipLimit);
            this.playerLoader.loadPlayerAccount(playerId, account, name, ip);

            event.setEncrypting(account != null && account.getAuth().isPremium());
        } catch (Exception e) {
            e.printStackTrace();

            event.setCancelled(true);
            event.setCancelReason(ProxyMessage.PROFILE_LOADING_ERROR.asFramedComponents(null, true));
        }
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            final UUID playerId = player.getUniqueId();
            final IHyriPlayer account = IHyriPlayer.get(playerId);

            event.setCancelled(true);

            this.connectToNetwork(player, account, event);
        }
    }

    @EventHandler
    public void onServerConnected(ServerConnectedEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final UUID playerId = player.getUniqueId();
        final IHyriPlayerSession session = IHyriPlayerSession.get(playerId);

        if (session == null) {
            player.disconnect(ProxyMessage.PROFILE_LOADING_ERROR.asFramedComponents(player, true));
            return;
        }

        session.setLastServer(session.getServer());
        session.setServer(event.getServer().getInfo().getName());
        session.update();

        if (!this.reconnections.remove(playerId)) {
            return;
        }

        // Reconnection part (if the player was in a game)
        final IHyriReconnectionData reconnectionData = HyriAPI.get().getServerManager().getReconnectionHandler().get(playerId);

        if (reconnectionData != null) {
            reconnectionData.reconnect();
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        final String ip = ((InetSocketAddress) event.getPlayer().getSocketAddress()).getAddress().getHostAddress();
        final AtomicInteger ipLimit = this.ipLimits.get(ip);

        if (ipLimit != null && ipLimit.decrementAndGet() <= 0) {
            this.ipLimits.remove(ip);
        }

        this.playerLoader.handleDisconnection(event.getPlayer(), false);
    }

    private void connectToNetwork(ProxiedPlayer player, IHyriPlayer account, ServerConnectEvent event) {
        final UUID playerId = player.getUniqueId();

        ServerInfo serverInfo = null;
        if (account != null && account.getAuth().isPremium()) { // If he is premium, connect him directly to a lobby
            final HyggServer lobby = HyriAPI.get().getLobbyAPI().getBestLobby();

            if (lobby != null) {
                // Load reconnection (if exists) before sending player to the lobby
                final IHyriReconnectionData reconnectionData = HyriAPI.get().getServerManager().getReconnectionHandler().get(playerId);

                if (reconnectionData != null) {
                    this.reconnections.add(playerId);
                }

                serverInfo = ProxyServer.getInstance().getServerInfo(lobby.getName());

                if (serverInfo != null) {
                    HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new PlayerJoinNetworkEvent(playerId));
                }
            }
        } else { // If he is crack, connect him first to a limbo to authenticate
            final HyggLimbo limbo = HyriAPI.get().getLimboManager().getBestLimbo(HyggLimbo.Type.LOGIN);

            if (limbo != null) {
                serverInfo = ProxyServer.getInstance().getServerInfo(limbo.getName());
            }
        }

        if (serverInfo == null) {
            this.playerLoader.handleDisconnection(player, true);

            player.disconnect(ProxyMessage.NO_SERVER.asFramedComponents(account, true));
            return;
        }

        event.setTarget(serverInfo);
        event.setCancelled(false);

        HyriAPI.get().getProxy().addPlayer(playerId);
    }

}
