package fr.hyriode.hyriapi.tools.chat;

import fr.hyriode.hyriapi.tools.util.PacketUtil;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActionBar {

    private final Map<Player, BukkitTask> permanentMessageTasks;
    private final Set<Player> players;

    private String message;

    public ActionBar(String message) {
        this.message = message;
        this.players = new ConcurrentSet<>();
        this.permanentMessageTasks = new HashMap<>();
    }

    public void send(Player player) {
        PacketUtil.sendPacket(player, this.getPacket());

        this.players.add(player);
    }

    public void send() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.send(player);
        }
    }

    public void sendPermanent(JavaPlugin plugin, Player player) {
        this.permanentMessageTasks.put(player, new BukkitRunnable() {
            @Override
            public void run() {
                send(player);
            }
        }.runTaskTimer(plugin, 0, 20));
    }

    public void sendPermanent(JavaPlugin plugin) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.sendPermanent(plugin, player);
        }
    }

    public void remove(Player player) {
        if (this.permanentMessageTasks.containsKey(player)) {
            this.permanentMessageTasks.get(player).cancel();
            this.permanentMessageTasks.remove(player);
        }
        PacketUtil.sendPacket(player, new PacketPlayOutChat(new ChatComponentText(""), (byte) 2));

        this.players.remove(player);
    }

    public void remove() {
        for (Player player : this.players) {
            this.remove(player);
        }
    }

    private Packet<?> getPacket() {
        return new PacketPlayOutChat(new ChatComponentText(this.message), (byte) 2);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
