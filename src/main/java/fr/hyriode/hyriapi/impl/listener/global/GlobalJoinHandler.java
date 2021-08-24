package fr.hyriode.hyriapi.impl.listener.global;

import fr.hyriode.hyriapi.impl.HyriPlugin;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.tools.title.Title;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/08/2021 at 20:35
 */
public class GlobalJoinHandler implements Listener {

    private final HyriPlugin plugin;

    public GlobalJoinHandler(HyriPlugin plugin) {
        this.plugin = plugin;

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        final IHyriPlayerManager playerManager = this.plugin.getAPI().getPlayerManager();
        final UUID uuid = event.getUniqueId();

        IHyriPlayer player = playerManager.getPlayer(event.getUniqueId());
        if (player == null) {
            player = playerManager.createPlayer(uuid);
        }

        if (player == null) {
            event.setKickMessage("Erreur lors du chargement de votre profil!");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        event.setJoinMessage("");

        Title.setTitle(player, ChatColor.DARK_AQUA + "Bienvenue sur " + ChatColor.AQUA + "Hyriode " + ChatColor.DARK_AQUA + "!", ChatColor.WHITE + "Bon jeu sur nos serveurs", 10, 60, 10);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.setQuitMessage("");
    }

    @EventHandler
    public void onPlayerKicked(PlayerKickEvent event) {
        event.setLeaveMessage("");
    }

}
