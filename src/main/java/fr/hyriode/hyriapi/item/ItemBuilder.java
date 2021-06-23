package fr.hyriode.hyriapi.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.hyriode.hyriapi.util.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;
    private final Map<Class<? extends Event>, Consumer<Supplier<? extends Event>>> events;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = this.itemStack.getItemMeta();
        this.events = new HashMap<>();
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(Material material, int amount, int data) {
        this(new ItemStack(material, amount, (byte) data));
    }

    public ItemBuilder(Potion potion, int amount) {
        this(potion.toItemStack(amount));
    }

    public ItemBuilder(Potion potion) {
        this(potion, 1);
    }

    public ItemBuilder withName(String name) {
        this.itemMeta.setDisplayName(name);

        return this;
    }

    public ItemBuilder withLore(List<String> lore) {
        this.itemMeta.setLore(lore);

        return this;
    }

    public ItemBuilder withLore(String... lore) {
        return this.withLore(Arrays.asList(lore));
    }

    public ItemBuilder withSkullOwner(UUID uuid) {
        final OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        final SkullMeta skullMeta = (SkullMeta) this.itemMeta;

        skullMeta.setOwner(player.getName());

        this.itemStack.setItemMeta(skullMeta);

        return this;
    }

    public ItemBuilder withCustomHead(String name) {
        try {
            final SkullMeta skullMeta = (SkullMeta) this.itemMeta;
            final GameProfile profile = new GameProfile(UUID.randomUUID(), null);

            profile.getProperties().put("textures", new Property("textures", name));

            Reflection.setField(skullMeta.getClass().getDeclaredField("profile"), skullMeta, profile);

            this.itemStack.setItemMeta(skullMeta);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ItemBuilder withItemFlags(ItemFlag... itemFlags) {
        this.itemMeta.addItemFlags(itemFlags);

        return this;
    }

    public ItemBuilder withAllItemFlags() {
        return this.withItemFlags(ItemFlag.values());
    }

    public ItemBuilder withEnchant(Enchantment enchant, int level, boolean show) {
        this.itemMeta.addEnchant(enchant, level, show);

        return this;
    }

    public ItemBuilder withEnchant(Enchantment enchant, int level) {
        return this.withEnchant(enchant, level, true);
    }

    public ItemBuilder withEnchant(Enchantment enchant) {
        return this.withEnchant(enchant, 1, true);
    }

    public ItemBuilder withHidingEnchantments() {
        return this.withItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public ItemBuilder withInfiniteDurability() {
        this.itemStack.setDurability(Short.MAX_VALUE);

        return this;
    }

    public ItemBuilder withLeatherArmorColor(Color color) {
        final LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) this.itemMeta;

        leatherArmorMeta.setColor(color);

        this.itemStack.setItemMeta(leatherArmorMeta);

        return this;
    }

    public ItemBuilder withEvent(Class<? extends Event> eventClass, Consumer<Supplier<? extends Event>> eventConsumer) {
        this.events.put(eventClass, eventConsumer);

        return this;
    }

    public ItemStack build(ItemManager itemManager) {
        this.itemStack.setItemMeta(this.itemMeta);

        if (itemManager != null) {
            itemManager.registerItemEvents(this.itemStack, this.events);
        }

        return this.itemStack;
    }

    public ItemStack build() {
        return this.build(null);
    }

    public Map<Class<? extends Event>, Consumer<Supplier<? extends Event>>> getEvents() {
        return this.events;
    }

}
