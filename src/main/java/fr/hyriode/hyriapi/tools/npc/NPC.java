package fr.hyriode.hyriapi.tools.npc;

import com.mojang.authlib.GameProfile;
import fr.hyriode.hyriapi.tools.hologram.Hologram;
import fr.hyriode.hyriapi.util.PacketUtil;
import fr.hyriode.hyriapi.util.reflection.entity.EnumItemSlot;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class NPC extends EntityPlayer {

    protected NPCInteractCallback interactCallback;

    protected final Map<EnumItemSlot, ItemStack> equipment;

    protected Hologram hologram;

    protected Set<Player> players;

    protected  boolean showingToAll;
    protected boolean trackingPlayer;
    protected Location location;

    protected final JavaPlugin plugin;

    public NPC(JavaPlugin plugin, Location location, World world, GameProfile gameProfile) {
        super(world.getServer().getServer(), (WorldServer) world, gameProfile, new PlayerInteractManager(world));
        this.plugin = plugin;
        this.trackingPlayer = false;
        this.showingToAll = true;
        this.players = new HashSet<>();
        this.equipment = new HashMap<>();

        this.setLocation(location);
    }

    public Location getLocation() {
        return this.location;
    }

    public NPC setLocation(Location location) {
        this.location = location;

        this.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketUtil.sendPacket(player, this.getTeleportPacket());
        }

        return this;
    }

    public boolean isTrackingPlayer() {
        return this.trackingPlayer;
    }

    public NPC setTrackingPlayer(boolean trackingPlayer) {
        this.trackingPlayer = trackingPlayer;

        return this;
    }

    public boolean isShowingToAll() {
        return this.showingToAll;
    }

    public NPC setShowingToAll(boolean showingToAll) {
        this.showingToAll = showingToAll;

        return this;
    }

    public Set<Player> getPlayers() {
        return this.players;
    }

    public NPC setPlayers(Set<Player> players) {
        this.players = players;

        return this;
    }

    public NPC addPlayer(Player player) {
        this.players.add(player);

        if (this.hologram != null) {
            this.hologram.addReceiver(player);

            this.hologram.generateLines();
        }

        return this;
    }

    public NPC removePlayer(Player player) {
        this.players.remove(player);

        if (this.hologram != null) {
            this.hologram.removeReceiver(player);

            this.hologram.generateLines();
        }

        return this;
    }

    public Hologram getHologram() {
        return this.hologram;
    }

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }

    public NPC setEquipment(EnumItemSlot slot, ItemStack itemStack) {
        this.equipment.put(slot, itemStack);

        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketUtil.sendPacket(player, this.getEquipmentPacket(slot, itemStack));
        }

        return this;
    }

    public NPCInteractCallback getInteractCallback() {
        return this.interactCallback;
    }

    public NPC setInteractCallback(NPCInteractCallback interactCallback) {
        this.interactCallback = interactCallback;

        return this;
    }

    public List<Packet<?>> getSpawnPackets() {
        final List<Packet<?>> packets = new ArrayList<>();

        packets.add(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this));
        packets.add(new PacketPlayOutNamedEntitySpawn(this));
        packets.add(new PacketPlayOutEntityHeadRotation(this, (byte) (this.yaw * 256.0F / 360.0F)));

        for (Map.Entry<EnumItemSlot, ItemStack> entry : this.equipment.entrySet()) {
            packets.add(this.getEquipmentPacket(entry.getKey(), entry.getValue()));
        }

        return packets;
    }

    public List<Packet<?>> getDestroyPackets() {
        final List<Packet<?>> packets = new ArrayList<>();

        packets.add(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this));
        packets.add(new PacketPlayOutEntityDestroy(this.getId()));

        return packets;
    }

    public List<Packet<?>> getRotationPackets(float yaw, float pitch) {
        final List<Packet<?>> packets = new ArrayList<>();

        packets.add(new PacketPlayOutEntity.PacketPlayOutEntityLook(this.getId(), (byte) (yaw * 256.0F / 360.0F), (byte) (pitch * 256.0F / 360.0F), false));
        packets.add(new PacketPlayOutEntityHeadRotation(this, (byte) (yaw * 256.0F / 360.0F)));

        return packets;
    }

    public Packet<?> getTeleportPacket() {
        return new PacketPlayOutEntityTeleport(this);
    }

    public Packet<?> getEquipmentPacket(EnumItemSlot slot, ItemStack itemStack) {
        return new PacketPlayOutEntityEquipment(this.getId(), slot.getSlot(), CraftItemStack.asNMSCopy(itemStack));
    }

    public Packet<?> getMetadataPacket() {
        return new PacketPlayOutEntityMetadata(this.getId(), this.getDataWatcher(), true);
    }

}
