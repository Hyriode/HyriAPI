package fr.hyriode.api.impl.server.join;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.CHyriAPIImpl;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.impl.common.player.HyriPlayerSession;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.player.event.PlayerJoinNetworkEvent;
import fr.hyriode.api.player.event.PlayerJoinServerEvent;
import fr.hyriode.api.player.event.PlayerQuitNetworkEvent;
import fr.hyriode.api.player.event.PlayerQuitServerEvent;
import fr.hyriode.api.player.nickname.IHyriNickname;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by Corentin
 * on 17/02/2022 at 18:36
 */
public class JoinListener implements Listener {

    private final CHyriAPIImpl hyriAPI;
    private final HyriJoinManager joinManager;

    public JoinListener() {
        this.hyriAPI = (CHyriAPIImpl) HyriAPI.get();
        this.joinManager = (HyriJoinManager) hyriAPI.getJoinManager();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        try {
            final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
            final UUID uuid = event.getUniqueId();

            if (playerManager.getPlayer(uuid) == null) {
                playerManager.createPlayer(Bukkit.getServer().getOnlineMode(), uuid, event.getName());
            }

            this.joinManager.onLogin(event);
        } catch (Exception e) {
            e.printStackTrace();
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "An error occurred while loading your profile!");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID playerId = player.getUniqueId();
        final IHyriPlayer account = IHyriPlayer.get(playerId);
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();

        if (account == null) {
            player.kickPlayer(ChatColor.RED + "An error occurred while loading your profile!");
            return;
        }

        if (account.getRank().is(HyriStaffRankType.ADMINISTRATOR)) {
            player.setOp(true);
        }

        event.setJoinMessage("");

        if (HyriAPI.get().getConfig().isDevEnvironment()) {
            final long loginTime = System.currentTimeMillis();
            final IHyriPlayerSession session = new HyriPlayerSession(playerId, loginTime);

            account.setName(player.getName());
            account.setLastLoginDate(loginTime);

            playerManager.setPlayerId(account.getName(), playerId);

            session.setLastServer(session.getServer());
            session.setServer(HyriAPI.get().getServer().getName());

            account.update();
            session.update();

            HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new PlayerJoinNetworkEvent(playerId));
        }

        this.hyriAPI.getLanguageManager().setCache(playerId, account.getSettings().getLanguage());
        this.joinManager.onJoin(player);

        HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new PlayerJoinServerEvent(playerId, HyriAPI.get().getServer().getName()));
        HyriAPI.get().getServer().addPlayer(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final UUID playerId = player.getUniqueId();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final IHyriPlayer account = IHyriPlayer.get(playerId);

        event.setQuitMessage("");

        this.joinManager.onLogout(player);

        if (account != null && HyriAPI.get().getConfig().isDevEnvironment()) {
            final IHyriPlayerSession session = IHyriPlayerSession.get(playerId);

            account.setPlayTime(account.getPlayTime() + (System.currentTimeMillis() - account.getLastLoginDate()));
            account.update();

            if (session != null) {
                final IHyriNickname nickname = session.getNickname();

                if (nickname != null) {
                    playerManager.getNicknameManager().removeUsedNickname(nickname.getName());
                }

                playerManager.deleteSession(playerId);
            }

            HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new PlayerQuitNetworkEvent(playerId));
        }

        this.hyriAPI.getLanguageManager().removeCache(playerId);

        HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new PlayerQuitServerEvent(playerId, HyriAPI.get().getServer().getName()));
        HyriAPI.get().getServer().removePlayer(playerId);
    }

}
