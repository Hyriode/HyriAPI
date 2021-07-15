package fr.hyriode.hyriapi.tools.scoreboard;

import fr.hyriode.hyriapi.tools.util.PacketUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ObjectiveSign extends Objective {

    private final Map<Integer, String> lines;

    public ObjectiveSign(String name, String displayName) {
        super(name, displayName);
        this.lines = new HashMap<>();
    }

    public void setLine(int line, String value) {
        this.scores.remove(this.getScore(this.lines.get(line)));
        this.scores.add(new Score(value, line));

        this.lines.put(line, value);
    }

    public void updateLines(boolean inverse) {
        final String oldName = this.getOldName();

        for (Player player : this.players) {
            this.create(player);
            this.updateScore(player, inverse);
            this.display(player);

            PacketUtil.sendPacket(player, this.getObjectivePacket(1, oldName));
        }
    }

    public void updateLines() {
        this.updateLines(true);
    }

    public void replaceScore(Score oldScore, Score newScore) {
        this.updateScore(newScore);
        this.removeScore(oldScore);
    }

}
