package fr.hyriode.hyriapi.tools.sign;

import fr.hyriode.hyriapi.tools.util.PacketUtil;
import fr.hyriode.hyriapi.tools.util.reflection.Reflection;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SignHandler implements Listener {

    private final SignManager signManager;

    public SignHandler(JavaPlugin plugin, SignManager signManager) {
        this.signManager = signManager;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        this.inject(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        this.uninject(player);
    }

    private void inject(Player player) {
        final ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
                if (packet instanceof PacketPlayInUpdateSign) {
                    final PacketPlayInUpdateSign updateSign = (PacketPlayInUpdateSign) packet;
                    final Map<UUID, Sign> signs = signManager.getSigns();

                    if (signs.containsKey(player.getUniqueId())) {
                        final Sign sign = signs.get(player.getUniqueId());
                        final BlockPosition blockPosition = (BlockPosition) Reflection.invokeField(updateSign, "a");
                        final IChatBaseComponent[] components = updateSign.b();
                        final List<String> lines = new ArrayList<>();

                        for (IChatBaseComponent component : components) {
                            lines.add(component.getText());
                        }

                        final PacketPlayOutBlockChange blockChangePacket = new PacketPlayOutBlockChange(((CraftWorld) player.getWorld()).getHandle(), blockPosition);

                        blockChangePacket.block = Blocks.AIR.getBlockData();

                        PacketUtil.sendPacket(player, blockChangePacket);

                        sign.getCompleteCallback().call(player, blockPosition, lines.toArray(new String[4]));
                        signs.remove(player.getUniqueId());
                    }
                }
                super.channelRead(ctx, packet);
            }
        };

        final ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();

        pipeline.addBefore("packet_handler", "SignPacketInjector", channelDuplexHandler);
    }

    private void uninject(Player player) {
        final ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();

        if (pipeline.get("SignPacketInjector") != null) {
            pipeline.remove("SignPacketInjector");
        }
    }

}
