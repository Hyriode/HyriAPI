package fr.hyriode.tools.scoreboard.team;

import fr.hyriode.tools.util.PacketUtil;
import fr.hyriode.tools.util.reflection.Reflection;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ScoreboardTeamHandler implements Listener {

    public ScoreboardTeamHandler(JavaPlugin plugin) {

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @SuppressWarnings("unchecked")
    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        for (ScoreboardTeam team : ScoreboardTeam.getTeams()) {
            PacketUtil.sendPacket(player, team.getTeamPacket(0));

            for (Player p : team.getPlayers()) {
                final Packet<?> packet = team.getTeamPacket(3);

                ((List<String>) Reflection.invokeField(packet, "g")).add(p.getName());

                PacketUtil.sendPacket(player, packet);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        for (ScoreboardTeam team : ScoreboardTeam.getTeams()) {
            if (team.getPlayers().contains(player)) {
                team.removePlayer(player);
            }
        }
    }

}
