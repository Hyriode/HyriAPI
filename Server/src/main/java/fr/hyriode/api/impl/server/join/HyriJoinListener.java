package fr.hyriode.api.impl.server.join;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.friend.HyriFriends;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.nickname.IHyriNickname;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.hydrion.client.HydrionClient;
import fr.hyriode.hydrion.client.module.FriendsModule;
import fr.hyriode.hydrion.client.module.PlayerModule;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by Corentin
 * on 17/02/2022 at 18:36
 */
public class HyriJoinListener implements Listener {

    private final HyggdrasilManager hyggdrasilManager;
    private final HydrionManager hydrionManager;

    private final HyriJoinManager joinManager;

    private PlayerModule playerModule;
    private FriendsModule friendsModule;

    public HyriJoinListener(HyggdrasilManager hyggdrasilManager, HydrionManager hydrionManager, HyriJoinManager joinManager) {
        this.hyggdrasilManager = hyggdrasilManager;
        this.hydrionManager = hydrionManager;
        this.joinManager = joinManager;

        if (this.hydrionManager.isEnabled()) {
            final HydrionClient client = this.hydrionManager.getClient();

            this.playerModule = client.getPlayerModule();
            this.friendsModule = client.getFriendsModule();
        }
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
        final UUID uuid = player.getUniqueId();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final IHyriPlayer account = playerManager.getPlayer(uuid);

        event.setJoinMessage("");

        if (HyriAPI.get().getConfiguration().isDevEnvironment()) {
            account.setName(player.getName());
            account.setLastLoginDate(new Date(System.currentTimeMillis()));
            account.setOnline(true);

            playerManager.setPlayerId(account.getName(), uuid);
        }

        account.setCurrentServer(HyriAPI.get().getServer().getName());

        if (this.hydrionManager.isEnabled()) {
            final IHyriPlayer hydrionPlayer = playerManager.getPlayerFromHydrion(uuid);

            if (hydrionPlayer != null) {
                final HyriRank rank = hydrionPlayer.getRank();

                if (rank.isSuperior(account.getRank().getPlayerType())) {
                    account.setRank(hydrionPlayer.getRank());
                }

                account.setHyriPlus(hydrionPlayer.getHyriPlus());
            }

            this.playerModule.setPlayer(uuid, HyriAPI.GSON.toJson(account));
        }

        account.update();

        this.joinManager.onJoin(player);

        this.hyggdrasilManager.sendData();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");

        this.onLeave(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerKicked(PlayerKickEvent event) {
        event.setLeaveMessage("");

        this.onLeave(event.getPlayer());
    }

    private void onLeave(Player player) {
        final UUID uuid = player.getUniqueId();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final IHyriPlayer account = playerManager.getPlayer(player.getUniqueId());

        if (account != null) {
            if (HyriAPI.get().getConfiguration().isDevEnvironment()) {
                account.setPlayTime(account.getPlayTime() + (System.currentTimeMillis() - account.getLastLoginDate().getTime()));
                account.setOnline(false);
                account.setLastPrivateMessagePlayer(null);

                final IHyriNickname nickname = account.getNickname();

                if (nickname != null) {
                    playerManager.getNicknameManager().removeUsedNickname(nickname.getName());

                    account.setNickname(null);
                }
            }

            account.setLastServer(HyriAPI.get().getServer().getName());
            account.setCurrentServer(null);

            if (this.hydrionManager.isEnabled()) {
                this.friendsModule.setFriends(uuid, HyriAPI.GSON.toJson(new HyriFriends(HyriAPI.get().getFriendManager().getFriends(uuid))));
            }

            account.update();
        }

        this.joinManager.onLogout(player);

        this.hyggdrasilManager.sendData();
    }

}
