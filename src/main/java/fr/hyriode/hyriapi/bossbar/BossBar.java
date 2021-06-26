package fr.hyriode.hyriapi.bossbar;

import fr.hyriode.hyriapi.util.PacketUtil;
import fr.hyriode.hyriapi.util.Reflection;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BossBar {

    protected int cooldown;
    protected final int delay;

    protected boolean visible;
    protected float progress = 1.0F;

    private EntityWither wither;

    private AtomicInteger actualPlayerTitle;
    protected List<String> titles;

    protected final Player player;

    public BossBar(JavaPlugin plugin, Player player, List<String> titles, int delay) {
        this.player = player;
        this.titles = titles;
        this.delay = delay;
        this.visible = false;
        this.cooldown = 0;

        this.witherTask().runTaskTimer(plugin, 20, 20);
    }

    public BossBar(JavaPlugin plugin, Player player, String title) {
        this(plugin, player, Collections.singletonList(title), 0);
    }

    protected void spawn() {
        final Location playerLocation = this.player.getLocation();
        final Location witherLocation = this.getWitherLocation(playerLocation);

        this.wither = new EntityWither(((CraftWorld) player.getWorld()).getHandle());

        this.wither.setCustomName(titles.get(0));
        this.wither.setHealth(this.progress * this.wither.getMaxHealth());
        this.wither.setInvisible(true);
        this.wither.setLocation(witherLocation.getX(), witherLocation.getY(), witherLocation.getZ(), witherLocation.getYaw(), witherLocation.getPitch());
        this.wither.r(880);

        PacketUtil.sendPacket(player, new PacketPlayOutSpawnEntityLiving(wither));

        this.actualPlayerTitle = new AtomicInteger(1);
        this.visible = true;
    }

    protected void destroy() {
        if (this.wither != null) {
            PacketUtil.sendPacket(player, new PacketPlayOutEntityDestroy(wither.getId()));

            this.visible = false;
        }
    }

    protected void updateMovement() {
        final Location location = this.getWitherLocation(this.player.getLocation());

        this.wither.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        PacketUtil.sendPacket(player, new PacketPlayOutEntityTeleport(this.wither));
    }

    protected void sendMetaData() {
        PacketUtil.sendPacket(this.player, new PacketPlayOutEntityMetadata(this.wither.getId(), this.wither.getDataWatcher(), true));
    }

    private BukkitRunnable witherTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                updateMovement();

                // Change title
                cooldown += 1;

                if (cooldown == delay) {
                    wither.setCustomName(titles.get(actualPlayerTitle.get()));

                    sendMetaData();

                    if (actualPlayerTitle.addAndGet(1) >= titles.size()) actualPlayerTitle.set(0);

                    cooldown = 0;
                }
            }
        };
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
        this.actualPlayerTitle = new AtomicInteger(0);
    }

    public void setProgress(float progress) {
        this.progress = progress;

        this.wither.setHealth(this.progress * this.wither.getMaxHealth());

        if (this.wither.getHealth() <= 1) {
            this.setVisible(false);
        } else {
            this.sendMetaData();
        }
    }

    public float getProgress() {
        return this.progress;
    }

    public List<String> getTitles() {
        return this.titles;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        if (this.visible != visible) {
            if (visible) {
                this.spawn();
            } else {
                this.destroy();
            }
        }
    }

    private Location getWitherLocation(Location base) {
        return base.add(base.getDirection().multiply(32));
    }

}
