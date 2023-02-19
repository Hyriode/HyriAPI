package fr.hyriode.api.guild.event.leveling;

import fr.hyriode.api.leveling.event.LevelingLevelEvent;
import org.bson.types.ObjectId;

/**
 * Created by AstFaster
 * on 11/02/2023 at 21:21
 */
public class GuildLevelEvent extends LevelingLevelEvent {

    private final ObjectId guildId;

    public GuildLevelEvent(ObjectId guildId, String leveling, int oldLevel, int newLevel) {
        super(leveling, oldLevel, newLevel);
        this.guildId = guildId;
    }

    public ObjectId getGuildId() {
        return this.guildId;
    }

}
