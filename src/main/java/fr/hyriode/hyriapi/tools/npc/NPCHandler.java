package fr.hyriode.hyriapi.tools.npc;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.util.PacketUtil;
import fr.hyriode.hyriapi.util.reflection.Reflection;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class NPCHandler implements Listener {

    private final NPCManager npcManager;

    public NPCHandler() {
        this.npcManager = HyriAPI.get().getNPCManager();

        final JavaPlugin plugin = HyriAPI.get().getPlugin();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        this.checkDistance(player, event.getFrom(), event.getTo());
        this.trackPlayer(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTeleport(PlayerTeleportEvent event) {
        final Player player = event.getPlayer();

        this.checkDistance(player, event.getFrom(), event.getTo());
        this.trackPlayer(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        this.inject(player);

        for (NPC npc : this.npcManager.getAllNpc().keySet()) {
            this.npcManager.sendNPC(player, npc);
        }

        this.trackPlayer(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        for (NPC npc : this.npcManager.getAllNpc().keySet()) {
            this.npcManager.removeNPC(player, npc);
        }

        this.uninject(player);
    }

    private void trackPlayer(Player player) {
        for (NPC npc : this.npcManager.getAllNpc().keySet()) {
            if (npc.isTrackingPlayer()) {
                final Location location = npc.getLocation();

                if (location.distance(player.getLocation()) <= 4.0D) {
                    final Vector direction = player.getLocation().toVector().subtract(npc.getLocation().toVector());

                    location.setDirection(direction);

                    npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

                    PacketUtil.sendPacket(player, npc.getTeleportPacket());

                    for (Packet<?> packet : npc.getRotationPackets(location.getYaw(), location.getPitch())) {
                        PacketUtil.sendPacket(player, packet);
                    }
                }
            }
        }
    }

    private void checkDistance(Player player, Location from, Location to) {
        for (NPC npc : this.npcManager.getAllNpc().keySet()) {
            if (from.distanceSquared(npc.getLocation()) > 2500 && to.distanceSquared(npc.getLocation()) < 2500) {
                this.npcManager.sendNPC(player, npc);
            } else if (from.distanceSquared(npc.getBukkitEntity().getLocation()) < 2500 && to.distanceSquared(npc.getBukkitEntity().getLocation()) > 2500) {
                this.npcManager.removeNPC(npc);
            }
        }
    }

    private void inject(Player player) {
        final ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
                if (packet.getClass() == PacketPlayInUseEntity.class) {
                    final int entityId = (int) Reflection.invokeField(packet, "a");

                    for (NPC npc : npcManager.getAllNpc().keySet()) {
                        if (npc.getId() == entityId) {
                            if (npc.getInteractCallback() != null) {
                                final PacketPlayInUseEntity.EnumEntityUseAction action = (PacketPlayInUseEntity.EnumEntityUseAction) Reflection.invokeField(packet, "action");

                                npc.getInteractCallback().call(action.equals(PacketPlayInUseEntity.EnumEntityUseAction.INTERACT), player);
                            }
                        }
                    }
                }
                super.channelRead(ctx, packet);
            }
        };

        final ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();

        pipeline.addBefore("packet_handler", "NPCPacketInjector", channelDuplexHandler);
    }

    private void uninject(Player player) {
        final ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();

        if (pipeline.get("NPCPacketInjector") != null) {
            pipeline.remove("NPCPacketInjector");
        }
    }

}