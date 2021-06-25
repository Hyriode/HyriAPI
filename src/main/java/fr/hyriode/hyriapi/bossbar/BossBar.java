package fr.hyriode.hyriapi.bossbar;

import fr.hyriode.hyriapi.util.PacketUtil;
import fr.hyriode.hyriapi.util.Reflection;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import sun.util.resources.cldr.xog.LocaleNames_xog;

import java.util.HashMap;
import java.util.Map;

public class BossBar {

    private final Map<Player, EntityWither> withers;

    private String title;

    public BossBar(JavaPlugin plugin, String title) {
        this.title = title;
        this.withers = new HashMap<>();

        this.teleportTask().runTaskTimer(plugin, 0, 10);
    }

    public void addPlayer(Player player) {
        final Location playerLocation = player.getLocation();
        final EntityWither wither = new EntityWither(((CraftWorld) player.getWorld()).getHandle());
        final Location witherLocation = this.makeLocation(playerLocation, player.getWorld());

        wither.setCustomName(this.title);
        wither.setInvisible(true);
        wither.setLocation(witherLocation.getX(), witherLocation.getY(), witherLocation.getZ(), 0, 0);

        PacketUtil.sendPacket(player, new PacketPlayOutSpawnEntityLiving(wither));

        this.withers.put(player, wither);
    }

    public void removePlayer(Player player) {
        final EntityWither wither = this.withers.get(player);

        this.withers.remove(player);

        PacketUtil.sendPacket(player, new PacketPlayOutEntityDestroy(wither.getId()));
    }

    public void setTitle(String title) {
        this.title = title;

        for (Map.Entry<Player, EntityWither> entry : this.withers.entrySet()) {
            final EntityWither wither = entry.getValue();

            wither.setCustomName(this.title);

            PacketUtil.sendPacket(entry.getKey(), new PacketPlayOutEntityMetadata(wither.getId(), wither.getDataWatcher(), true));
        }
    }

    public void setProgress(double progress) {
        for (Map.Entry<Player, EntityWither> entry : this.withers.entrySet()) {
            final EntityWither wither = entry.getValue();

            wither.setHealth((float) (progress * wither.getMaxHealth()));

            PacketUtil.sendPacket(entry.getKey(), new PacketPlayOutEntityMetadata(wither.getId(), wither.getDataWatcher(), true));
        }
    }

    private BukkitRunnable teleportTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Player, EntityWither> entry : withers.entrySet()) {
                    try {
                        final Player player = entry.getKey();
                        final EntityWither wither = entry.getValue();
                        final Location location = makeLocation(player.getLocation(), player.getWorld());
                        final PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport();

                        Reflection.setField(teleportPacket.getClass().getDeclaredField("a"), teleportPacket, wither.getId());
                        Reflection.setField(teleportPacket.getClass().getDeclaredField("b"), teleportPacket, (int) (location.getX() * 32.0D));
                        Reflection.setField(teleportPacket.getClass().getDeclaredField("c"), teleportPacket, (int) (location.getY() * 32.0D));
                        Reflection.setField(teleportPacket.getClass().getDeclaredField("d"), teleportPacket, (int) (location.getZ() * 32.0D));
                        Reflection.setField(teleportPacket.getClass().getDeclaredField("e"), teleportPacket, (byte) (int) (location.getYaw() * 256.0F / 360.0F));
                        Reflection.setField(teleportPacket.getClass().getDeclaredField("f"), teleportPacket, (byte) (int) (location.getPitch() * 256.0F / 360.0F));

                        PacketUtil.sendPacket(player, teleportPacket);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private Location makeLocation(Location base, World world) {
        return base.getDirection().multiply(256).add(base.toVector()).toLocation(world);
    }

}
