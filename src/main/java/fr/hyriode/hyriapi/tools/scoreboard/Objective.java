package fr.hyriode.hyriapi.tools.scoreboard;

import fr.hyriode.hyriapi.tools.util.PacketUtil;
import fr.hyriode.hyriapi.tools.util.reflection.Reflection;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Objective {

    protected ObjectiveSlot slot = ObjectiveSlot.SIDEBAR;

    protected final Queue<Score> scores;
    protected final Set<Player> players;

    protected String displayName;
    protected String name;

    public Objective(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
        this.players = new HashSet<>();
        this.scores = new ConcurrentLinkedQueue<>();
    }

    public void init(Player player) {
        this.create(player);
        this.display(player);
    }

    public void destroy(Player player) {
        PacketUtil.sendPacket(player, this.getObjectivePacket(1, this.name));
    }

    protected void create(Player player) {
        PacketUtil.sendPacket(player, this.getObjectivePacket(0, this.name));
    }

    protected void display(Player player) {
        PacketUtil.sendPacket(player, this.getObjectiveDisplayPacket());
    }

    public String getName() {
        return this.name;
    }

    protected String getOldName() {
        String old = this.name;

        if (this.name.endsWith("1")) {
            this.name = this.name.substring(0, this.name.length() - 1);
        } else {
            this.name += "1";
        }

        return old;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;

        for (Player player : this.players) {
            PacketUtil.sendPacket(player, this.getObjectivePacket(2, this.name));
        }
    }

    public void addPlayer(Player player) {
        this.players.add(player);

        this.init(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);

        this.destroy(player);
    }

    public Set<Player> getPlayers() {
        return this.players;
    }

    public void updateScore(String score) {
        this.updateScore(this.getScore(score));
    }

    public void updateScore(boolean force) {
        if (force) {
            final String oldName = this.getOldName();

            for (Player player : this.players) {
                this.create(player);
                this.updateScore(player, false);
                this.display(player);

                PacketUtil.sendPacket(player, this.getObjectivePacket(1, oldName));
            }
        } else {
            for (Player player : this.players) {
                this.updateScore(player, false);
            }
        }
    }

    protected void updateScore(Player player, boolean inverse) {
        if (!inverse) {
            for (Score score : this.scores) {
                PacketUtil.sendPacket(player, this.getScorePacket(score.getPlayerName(), score.getScore(), PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE));
            }
        } else {
            for (Score score : this.scores) {
                 PacketUtil.sendPacket(player, this.getScorePacket(score.getPlayerName(), this.scores.size() - score.getScore() - 1, PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE));
            }
        }
    }

    protected void updateScore(Score score) {
        for (Player player : this.players) {
            PacketUtil.sendPacket(player, this.getScorePacket(score.getPlayerName(), score.getScore(), PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE));
        }
    }

    public void removeScore(Score score) {
        this.scores.remove(score);

        for (Player player : this.players) {
            PacketUtil.sendPacket(player, this.getScorePacket(score.getPlayerName(), score.getScore(), PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE));
        }
    }

    public void removeScore(String score) {
        this.removeScore(this.getScore(score));
    }

    public void clearScores() {
        this.scores.clear();
    }

    public Score getScore(String playerName) {
        for (Score score : this.scores) {
            if (score.getPlayerName().equals(playerName)) {
                return score;
            }
        }

        final Score score = new Score(playerName, 0);

        this.scores.add(score);

        return score;
    }

    public Queue<Score> getScores() {
        return this.scores;
    }

    public ObjectiveSlot getSlot() {
        return this.slot;
    }

    public void setSlot(ObjectiveSlot slot) {
        this.slot = slot;
    }

    protected Packet<?> getObjectivePacket(int mode, String name) {
        final PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();

        Reflection.setField("a", packet, name);
        Reflection.setField("b", packet, this.displayName);
        Reflection.setField("c", packet, IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        Reflection.setField("d", packet, mode);

        return packet;
    }

    protected Packet<?> getObjectiveDisplayPacket() {
        final PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();

        Reflection.setField("a", packet, this.slot.getId());
        Reflection.setField("b", packet, this.name);

        return packet;
    }

    protected Packet<?> getScorePacket(String scoreName, int scoreValue, Object action) {
        final PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore();

        Reflection.setField("a", packet, scoreName);
        Reflection.setField("b", packet, this.name);
        Reflection.setField("c", packet, scoreValue);
        Reflection.setField("d", packet, action);

        return packet;
    }

}
