package fr.hyriode.hyriapi.impl;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.tools.bossbar.BossBarHandler;
import fr.hyriode.hyriapi.tools.inventory.InventoryHandler;
import fr.hyriode.hyriapi.tools.item.ItemHandler;
import fr.hyriode.hyriapi.tools.npc.NPCHandler;
import fr.hyriode.hyriapi.tools.scoreboard.team.ScoreboardTeamHandler;
import org.bukkit.plugin.java.JavaPlugin;

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
        new InventoryHandler(this);
        new NPCHandler();
        new BossBarHandler(this);
        new ScoreboardTeamHandler(this);
    }

    @Override
    public void onDisable() {

    }

    public HyriAPI getAPI() {
        return this.api;
    }

}
