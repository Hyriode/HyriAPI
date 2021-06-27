package fr.hyriode.hyriapi;

import fr.hyriode.hyriapi.bossbar.BossBarHandler;
import fr.hyriode.hyriapi.bossbar.BossBarManager;
import fr.hyriode.hyriapi.item.ItemHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class HyriAPI extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        new ItemHandler(this);
        new BossBarHandler(this);

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        BossBarManager.setBar(this, player, Arrays.asList(ChatColor.GOLD + "MyCustom Bar", ChatColor.GREEN + "MyCustom Bar"), 1, 20, true);

        new BukkitRunnable() {
            @Override
            public void run() {
                BossBarManager.setBarTitles(player,  Arrays.asList(ChatColor.BLUE + "New TITLE !" + ChatColor.GOLD + " OH MY GOD UWU", ChatColor.RED + "New TITLE !" + ChatColor.DARK_GRAY + " OH MY GOD UWU"));
            }
        }.runTaskLater(this, 100);

    }

}
