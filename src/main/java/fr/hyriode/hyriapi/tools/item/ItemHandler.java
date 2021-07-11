package fr.hyriode.hyriapi.tools.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class ItemHandler {

    private final List<Listener> listeners;

    private final JavaPlugin plugin;

    public ItemHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.listeners = new ArrayList<>();

        this.addListeners();
        this.registerListeners();
    }

    private void addListeners() {
        this.listeners.add(new Listener() {
            @EventHandler
            public void onInteract(PlayerInteractEvent event) {
                execute(event.getItem(), event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof Player) {
                    execute(event.getCurrentItem(), event);
                }
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onItemBreak(PlayerItemBreakEvent event) {
                execute(event.getBrokenItem(), event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onItemConsume(PlayerItemConsumeEvent event) {
                execute(event.getItem(), event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onItemDamage(PlayerItemDamageEvent event) {
                execute(event.getItem(), event);
            }
        });
        this.listeners.add(new Listener() {
           @EventHandler
           public void onItemDrop(PlayerDropItemEvent event) {
               execute(event.getItemDrop().getItemStack(), event);
           }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onItemPickup(PlayerPickupItemEvent event) {
                execute(event.getItem().getItemStack(), event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onItemEnchant(EnchantItemEvent event) {
                execute(event.getItem(), event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onItemDespawn(ItemDespawnEvent event) {
                execute(event.getEntity().getItemStack(), event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onFurnaceBurn(FurnaceBurnEvent event) {
                execute(event.getFuel(), event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onFurnaceSmelt(FurnaceSmeltEvent event) {
                execute(event.getResult(), event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onPlayerFish(PlayerFishEvent event) {
                execute(event.getPlayer().getItemInHand(), event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onPotionSplash(PotionSplashEvent event){
                execute(event.getPotion().getItem(), event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onPrepareItemEnchant(PrepareItemEnchantEvent event){
                execute(event.getItem(), event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onBlockPlace(BlockPlaceEvent event){
                execute(event.getItemInHand(), event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onBlockBreak(BlockBreakEvent event){
                execute(event.getPlayer().getItemInHand(), event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onBlockDispense(BlockDispenseEvent event) {
                execute(event.getItem(), event);
            }
        });
    }

    private void execute(ItemStack itemStack, Event event) {
        final ItemConsumer<ItemSupplier<? extends Event>> eventConsumer = this.getEvent(itemStack, event.getClass());

        if (eventConsumer != null) {
            eventConsumer.accept(() -> event);
        }
    }

    private boolean containsEvent(ItemStack itemStack) {
        return itemStack != null && itemStack.getType() != Material.AIR && new ItemBuilder(itemStack).nbt().hasTag("ItemEvent");
    }

    private ItemConsumer<ItemSupplier<? extends Event>> getEvent(ItemStack itemStack, Class<? extends Event> eventClass) {
        if (this.containsEvent(itemStack)) {
            final ItemBuilder itemBuilder = new ItemBuilder(itemStack);
            final ItemData itemData = (ItemData) this.deserialize(itemBuilder.nbt().getByteArray("ItemEvent"));

            return itemData.getEventConsumer(eventClass);
        }

        return event -> {};
    }

    private Object deserialize(byte[] bytes) {
        try {
            final ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
            final ObjectInputStream objectInputStream = new ObjectInputStream(arrayInputStream);
            final Object result = objectInputStream.readObject();

            objectInputStream.close();

            return result;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    private void registerListeners() {
        this.listeners.forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, this.plugin));
    }

}