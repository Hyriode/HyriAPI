package fr.hyriode.hyriapi;

import fr.hyriode.hyriapi.bossbar.BossBarHandler;
import fr.hyriode.hyriapi.bossbar.BossBarManager;
import fr.hyriode.hyriapi.item.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class HyriAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        new ItemManager(this);
        new BossBarHandler(this);
    }

    @Override
    public void onDisable() {

    }

}
