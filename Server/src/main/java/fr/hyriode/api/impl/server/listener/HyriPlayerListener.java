package fr.hyriode.api.impl.server.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
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
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by Corentin
 * on 17/02/2022 at 18:36
 */
public class HyriPlayerListener implements Listener {

    private final HyggdrasilManager hyggdrasilManager;
    private final HydrionManager hydrionManager;
    private final PlayerModule playerModule;
    private final FriendsModule friendsModule;

    public HyriPlayerListener(HyggdrasilManager hyggdrasilManager, HydrionManager hydrionManager) {
        this.hyggdrasilManager = hyggdrasilManager;
        this.hydrionManager = hydrionManager;

        final HydrionClient client = this.hydrionManager.getClient();

        this.playerModule = client.getPlayerModule();
        this.friendsModule = client.getFriendsModule();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        try {
            final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
            final UUID uuid = event.getUniqueId();

            if (playerManager.getPlayer(uuid) == null) {
                playerManager.createPlayer(true, uuid, event.getName());
            }
        } catch (Exception e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "An error occurred while loading your profile!");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final IHyriPlayer account = playerManager.getPlayer(player.getUniqueId());

        if (HyriAPI.get().getConfiguration().isDevEnvironment()) {
            account.setName(player.getName());
            account.setLastLoginDate(new Date(System.currentTimeMillis()));
            account.setOnline(true);

            playerManager.setPlayerId(account.getName(), account.getUniqueId());
        }

        final HyriRank rank = account.getRank();
        final String customName = rank.getPrefix() + ChatColor.WHITE + HyriRank.SEPARATOR + rank.getMainColor().toString() + account.getCustomName();

        account.setCurrentServer(HyriAPI.get().getServer().getName());
        account.setNameWithRank(customName);
        account.update();

        this.hyggdrasilManager.sendData();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final IHyriPlayer account = playerManager.getPlayer(player.getUniqueId());

        if (account != null) {
            if (HyriAPI.get().getConfiguration().isDevEnvironment()) {
                account.setPlayTime(account.getPlayTime() + (System.currentTimeMillis() - account.getLastLoginDate().getTime()));
                account.setOnline(false);
            }

            account.setLastServer(HyriAPI.get().getServer().getName());
            account.setCurrentServer(null);
            account.update();

            if (this.hydrionManager.isEnabled()) {
                this.playerModule.setPlayer(uuid, HyriAPI.GSON.toJson(account));
                this.friendsModule.setFriends(uuid, HyriAPI.GSON.toJson(HyriAPI.get().getFriendManager().getFriends(uuid)));
            }
        }

        this.hyggdrasilManager.sendData();
    }

}
