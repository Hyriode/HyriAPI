package fr.hyriode.tools.scoreboard.team;

import fr.hyriode.tools.util.PacketUtil;
import fr.hyriode.tools.util.reflection.Reflection;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScoreboardTeam {

    private static final List<ScoreboardTeam> teams = new ArrayList<>();

    private final Set<Player> players;

    private String prefix;
    private String name;

    public ScoreboardTeam(String name, String prefix) {
        this.name = name;
        this.prefix = prefix.endsWith(" ") ? prefix : prefix + " ";
        this.players = new HashSet<>();
    }

    public void create() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketUtil.sendPacket(player, this.getTeamPacket(0));
        }
        teams.add(this);
    }

    public void update() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketUtil.sendPacket(player, this.getTeamPacket(2));
        }
    }

    public void remove() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketUtil.sendPacket(player, this.getTeamPacket(1));
        }
        teams.remove(this);
    }

    public void setFriendlyFire(boolean value) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketUtil.sendPacket(player, this.getTeamPacket(1, (value ? 1 : 0)));
        }
    }

    @SuppressWarnings("unchecked")
    public void addPlayer(Player player) {
        this.players.add(player);

        final Packet<?> packet = this.getTeamPacket(3);

        ((List<String>) Reflection.invokeField(packet, "g")).add(player.getName());

        for (Player p : Bukkit.getOnlinePlayers()) {
            PacketUtil.sendPacket(p, packet);
        }
    }

    @SuppressWarnings("unchecked")
    public void removePlayer(Player player) {
        this.players.remove(player);

        final Packet<?> packet = this.getTeamPacket(4);

        ((List<String>) Reflection.invokeField(packet, "g")).remove(player.getName());

        for (Player p : Bukkit.getOnlinePlayers()) {
            PacketUtil.sendPacket(p, packet);
        }
    }

    protected Packet<?> getTeamPacket(int mode, int friendlyFire) {
        final PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

        Reflection.setField("a", packet, this.name);
        Reflection.setField("b", packet, "");
        Reflection.setField("c", packet, this.prefix);
        Reflection.setField("d", packet, "");
        Reflection.setField("e", packet, "always");
        Reflection.setField("f", packet, 0);
        Reflection.setField("h", packet, mode);
        Reflection.setField("i", packet, friendlyFire);

        return packet;
    }

    protected Packet<?> getTeamPacket(int mode) {
        return this.getTeamPacket(mode, 0);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Set<Player> getPlayers() {
        return this.players;
    }

    public static List<ScoreboardTeam> getTeams() {
        return teams;
    }
}
