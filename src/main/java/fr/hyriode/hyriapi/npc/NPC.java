package fr.hyriode.hyriapi.npc;

import com.mojang.authlib.GameProfile;
import fr.hyriode.hyriapi.util.PacketUtil;
import fr.hyriode.hyriapi.util.reflection.Reflection;
import fr.hyriode.hyriapi.util.reflection.entity.EnumItemSlot;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class NPC extends EntityPlayer {

    protected NPCInteractCallback interactCallback;

    protected final Map<EnumItemSlot, ItemStack> equipment;

    protected Set<UUID> players;

    protected boolean trackingPlayer;
    protected Location location;
    protected String name;

    public NPC(String name, Location location, World world, GameProfile gameProfile) {
        super(world.getServer().getServer(), (WorldServer) world, gameProfile, new PlayerInteractManager(world));
        this.name = name;
        this.trackingPlayer = false;
        this.players = new HashSet<>();
        this.equipment = new HashMap<>();

        this.setLocation(location);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public NPC setName(String name) {
        this.name = name;

        this.sendGameInfo();

        return this;
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

    public Set<UUID> getPlayers() {
        return this.players;
    }

    public NPC setPlayers(Set<UUID> players) {
        this.players = players;

        return this;
    }

    public NPC addPlayer(UUID player) {
        this.players.add(player);

        return this;
    }

    public NPC removePlayer(UUID player) {
        this.players.remove(player);

        return this;
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

    public void setInteractCallback(NPCInteractCallback interactCallback) {
        this.interactCallback = interactCallback;
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

    private void sendGameInfo() {
        NPCManager.removeNPC(this);

        final GameProfile gameProfile = this.getProfile();

        Reflection.setField(Reflection.getField(gameProfile.getClass(), "name"), gameProfile, this.name);

        NPCManager.sendNPC(this);
    }

}
