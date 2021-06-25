package fr.hyriode.hyriapi;

import fr.hyriode.hyriapi.bossbar.BossBar;
import fr.hyriode.hyriapi.item.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class HyriAPI extends JavaPlugin implements Listener {

    private ItemManager itemManager;

    @Override
    public void onEnable() {
        this.itemManager = new ItemManager(this);

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final BossBar bossBar = new BossBar(this, ChatColor.DARK_AQUA + "okml");

        bossBar.setProgress(0.33D);
        bossBar.addPlayer(event.getPlayer());
    }

    public ItemManager getItemManager() {
        return this.itemManager;
    }
}
