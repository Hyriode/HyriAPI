package fr.hyriode.api.guild.event.leveling;

import fr.hyriode.api.leveling.event.LevelingXPEvent;
import org.bson.types.ObjectId;

/**
 * Created by AstFaster
 * on 11/02/2023 at 21:22
 */
public class GuildXPEvent extends LevelingXPEvent {

    private final ObjectId guildId;

    public GuildXPEvent(ObjectId guildId, String leveling, double oldExperience, double newExperience) {
        super(leveling, oldExperience, newExperience);
        this.guildId = guildId;
    }

    public ObjectId getGuildId() {
        return this.guildId;
    }

}
