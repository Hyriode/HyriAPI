package fr.hyriode.hyriapi.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    private final ItemNBT itemNBT;
    private final ItemData itemData;
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = this.itemStack.getItemMeta();
        this.itemData = new ItemData();
        this.itemNBT = new ItemNBT(this.itemStack);
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

            final Field profileField = skullMeta.getClass().getDeclaredField("profile");

            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);

            this.itemStack.setItemMeta(skullMeta);
        } catch (NoSuchFieldException | IllegalAccessException e) {
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

    public ItemBuilder withEvent(Class<? extends Event> eventClass, ItemConsumer<ItemSupplier<? extends Event>> eventConsumer) {
        this.itemData.addEvent(eventClass, eventConsumer);

        return this;
    }

    public ItemBuilder clone() {
        return new ItemBuilder(this.itemStack);
    }

    public ItemNBT nbt() {
        this.itemStack.setItemMeta(this.itemMeta);

        return new ItemNBT(this.itemStack);
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);

        return new ItemNBT(this.itemStack).setByteArray("ItemEvent", this.serializeData()).build();
    }

    private byte[] serializeData() {
        try {
            final ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);

            objectOutputStream.writeObject(this.itemData);
            objectOutputStream.flush();
            objectOutputStream.close();

            return arrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[] {};
    }

}