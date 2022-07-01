package fr.hyriode.api.impl.server.join;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriendManager;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.event.PlayerJoinNetworkEvent;
import fr.hyriode.api.player.event.PlayerQuitNetworkEvent;
import fr.hyriode.api.player.nickname.IHyriNickname;
import fr.hyriode.api.rank.type.HyriStaffRankType;
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
public class HyriJoinListener implements Listener {

    private final IHyriFriendManager friendManager;

    private final HyggdrasilManager hyggdrasilManager;

    private final HyriJoinManager joinManager;

    public HyriJoinListener(HyggdrasilManager hyggdrasilManager, HyriJoinManager joinManager) {
        this.hyggdrasilManager = hyggdrasilManager;
        this.joinManager = joinManager;
        this.friendManager = HyriAPI.get().getFriendManager();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        try {
            final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
            final UUID uuid = event.getUniqueId();

            if (playerManager.getPlayer(uuid) == null) {
                playerManager.createPlayer(true, uuid, event.getName());
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
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final IHyriPlayer account = playerManager.getPlayerFromRedis(playerId);

        if (account == null) {
            player.kickPlayer(ChatColor.RED + "An error occurred while loading your profile!");
            return;
        }

        final IHyriParty party = HyriAPI.get().getPartyManager().getParty(account.getParty());

        event.setJoinMessage("");

        if (HyriAPI.get().getConfiguration().isDevEnvironment()) {
            account.setName(player.getName());
            account.setLastLoginDate(new Date(System.currentTimeMillis()));

            playerManager.setPlayerId(account.getName(), playerId);

            this.friendManager.saveFriends(this.friendManager.createHandler(playerId));

            account.setLastServer(account.getCurrentServer());
            account.setOnline(true);
            account.setCurrentServer(HyriAPI.get().getServer().getName());

            if (party != null && party.isLeader(playerId)) {
                party.setServer(account.getCurrentServer());
            }

            playerManager.savePrefix(playerId, account.getNameWithRank());

            HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new PlayerJoinNetworkEvent(playerId));
        }

        if (account.getRank().is(HyriStaffRankType.ADMINISTRATOR)) {
            player.setOp(true);
        }

        this.joinManager.onJoin(player);

        HyriAPI.get().getServer().addPlayer(player.getUniqueId());

        this.hyggdrasilManager.sendData();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");

        this.onLeave(event.getPlayer());
    }

    private void onLeave(Player player) {
        final UUID playerId = player.getUniqueId();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final IHyriPlayer account = playerManager.getPlayerFromRedis(player.getUniqueId());

        if (account != null && HyriAPI.get().getConfiguration().isDevEnvironment()) {
            account.setPlayTime(account.getPlayTime() + (System.currentTimeMillis() - account.getLastLoginDate().getTime()));
            account.setOnline(false);
            account.setLastPrivateMessagePlayer(null);

            final IHyriNickname nickname = account.getNickname();

            if (nickname != null) {
                playerManager.getNicknameManager().removeUsedNickname(nickname.getName());

                account.setNickname(null);
            }

            account.update();

            HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new PlayerQuitNetworkEvent(playerId));
        }

        this.joinManager.onLogout(player);

        HyriAPI.get().getServer().removePlayer(playerId);

        this.hyggdrasilManager.sendData();
    }

}
