package fr.hyriode.hyriapi;

import fr.hyriode.hyriapi.item.ItemBuilder;
import fr.hyriode.hyriapi.item.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
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
        final ItemBuilder item = new ItemBuilder(Material.DIAMOND_ORE, 12)
                .withName(ChatColor.RED + "Mon super trop supra bien item")
                .withLore(ChatColor.YELLOW + "ton petit chevalet", ChatColor.AQUA + "poule")
                .withEnchant(Enchantment.DAMAGE_ALL)
                .withEvent(PlayerInteractEvent.class, supplier -> {
                    final PlayerInteractEvent interactEvent = (PlayerInteractEvent) supplier.get();

                    interactEvent.getPlayer().sendMessage("koul");
                });

        event.getPlayer().getInventory().addItem(item.build(this.itemManager));
    }

    public ItemManager getItemManager() {
        return this.itemManager;
    }
}
