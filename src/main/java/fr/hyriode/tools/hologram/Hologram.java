package fr.hyriode.tools.hologram;

import fr.hyriode.tools.util.PacketUtil;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hologram {

    private final double distance = 0.24D;
    private final double rangeView = 60.0D;

    private final BukkitTask linesTask;

    private final Map<Integer, EntityArmorStand> entities;
    private final Map<Player, Boolean> receivers;

    private boolean linesChanged;

    private Location location;
    private List<String> lines;

    public Hologram(JavaPlugin plugin, String... lines) {
        this.lines = Arrays.asList(lines);
        this.linesChanged = true;
        this.receivers = new HashMap<>();
        this.entities = new HashMap<>();

        this.linesTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::sendLines, 10L, 10L);
    }

    public void addReceiver(Player player) {
        boolean range = false;

        if (player.getLocation().getWorld() == this.location.getWorld() && player.getLocation().distance(this.location) <= this.rangeView) {
            range = true;

            this.sendLines(player);
        }

        this.receivers.put(player, range);
    }

    public void removeReceiver(Player player) {
        this.receivers.remove(player);

        this.removeLines(player);
    }

    public void generateLines(Location location) {
        final Location first = location.clone().add(0, ((float) this.lines.size() / 2) * this.distance, 0);

        for (int i = 0; i < this.lines.size(); i++) {
            this.entities.put(i, this.generateEntitiesForLine(first.clone(), this.lines.get(i)));

            first.subtract(0, this.distance, 0);
        }

        this.location = location;
    }

    public void generateLines() {
        this.generateLines(this.location);
    }

    public void sendLines(Player player) {
        for (int i = 0; i < this.lines.size(); i++) {
            this.sendPacketForLine(player, i);
        }
    }

    public void sendLines() {
        for (Player player : this.receivers.keySet()) {
            final boolean range = player.getLocation().getWorld() == this.location.getWorld() && player.getLocation().distance(this.location) <= this.rangeView;
            final boolean wasRange = this.receivers.get(player);

            if (this.linesChanged && range) {
                this.sendLines(player);

                this.linesChanged = false;
            } else if (wasRange == range) {
                continue;
            } else if (wasRange) {
                this.removeLines(player);
            } else {
                this.sendLines(player);
            }

            this.receivers.put(player, range);
        }
    }

    public void removeLine(Player player, int line) {
        final EntityArmorStand armorStand = this.entities.get(line);

        if (armorStand != null) {
            PacketUtil.sendPacket(player, new PacketPlayOutEntityDestroy(armorStand.getId()));
        }
    }

    public void removeLines(Player player) {
        for (int i = 0; i < this.lines.size(); i++) {
            this.removeLine(player, i);
        }
    }

    public void removeLines() {
        for (Player player : this.receivers.keySet()) {
            this.removeLines(player);
        }
    }

    private EntityArmorStand generateEntitiesForLine(Location location, String text) {
        final EntityArmorStand armorStand = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle());

        armorStand.setSize(0.00001F, 0.00001F);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setCustomName(text);
        armorStand.setCustomNameVisible(true);
        armorStand.setLocation(location.getX(), location.getY() - 2, location.getZ(), 0, 0);

        return armorStand;
    }

    private void sendPacketForLine(Player player, int line) {
        final EntityArmorStand armorStand = this.entities.get(line);

        if (armorStand != null) {
            PacketUtil.sendPacket(player, new PacketPlayOutSpawnEntity(armorStand, 78));
            PacketUtil.sendPacket(player, new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true));
        }
    }

    public void change(String... lines) {
        this.removeLines();

        this.clearLines();
        this.clearEntities();

        this.lines = Arrays.asList(lines);
        this.linesChanged = true;

        this.generateLines();
    }

    public void destroy() {
        this.removeLines();

        this.clearEntities();
        this.clearLines();

        this.location = null;
    }

    public void fullDestroy() {
        this.destroy();

        this.receivers.clear();
        this.linesTask.cancel();
    }

    public void clearEntities() {
        this.entities.clear();
    }

    public void clearLines() {
        this.lines.clear();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public List<String> getLines() {
        return this.lines;
    }
}
