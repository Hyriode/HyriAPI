package fr.hyriode.hyriapi.impl;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.bossbar.BossBarHandler;
import fr.hyriode.hyriapi.item.ItemHandler;
import fr.hyriode.hyriapi.npc.NPC;
import fr.hyriode.hyriapi.npc.NPCHandler;
import fr.hyriode.hyriapi.npc.NPCManager;
import fr.hyriode.hyriapi.util.reflection.entity.EnumItemSlot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class HyriAPIPlugin extends JavaPlugin {

    private HyriAPI api;

    @Override
    public void onEnable() {
        System.out.println("#=============[Welcome to HyriAPI]=============#");
        System.out.println("# HyriAPI is now starting... Please read       #");
        System.out.println("# carefully all information coming from it.    #");
        System.out.println("#==============================================#");
        System.out.println("# HyriAPI is a production of Hyriode           #");
        System.out.println("# Authors: AstFaster, Keinz_                   #");
        System.out.println("#==============================================#");

        this.api = new HyriAPIImplementation(this);

        new ItemHandler(this);
        new BossBarHandler(this);
        new NPCHandler(this);
    }

    @Override
    public void onDisable() {

    }

    public HyriAPI getAPI() {
        return this.api;
    }

}
