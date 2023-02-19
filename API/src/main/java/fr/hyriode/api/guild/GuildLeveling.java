package fr.hyriode.api.guild;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.guild.event.leveling.GuildLevelEvent;
import fr.hyriode.api.guild.event.leveling.GuildXPEvent;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;

import java.io.IOException;

/**
 * Created by AstFaster
 * on 11/02/2023 at 16:17
 */
public class GuildLeveling implements IHyriLeveling, MongoSerializable, DataSerializable {

    public static final String NAME = "guild";

    public static final Algorithm ALGORITHM = new Algorithm() {
        @Override
        public int experienceToLevel(double experience) {
            int level = 0;

            while (this.levelToExperience(level + 1) <= experience) {
                level++;
            }
            return level;
        }

        @Override
        public double levelToExperience(int level) {
            if (level == 0) {
                return 0.0D;
            }
            return Math.ceil(180 * level * Math.sqrt(level) + 300);
        }
    };

    @Expose
    private double experience;

    private final IHyriGuild guild;

    public GuildLeveling(IHyriGuild guild) {
        this.guild = guild;
    }

    @Override
    public void save(MongoDocument document) {
        document.append("experience", this.experience);
    }

    @Override
    public void load(MongoDocument document) {
        this.experience = document.getDouble("experience");
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeDouble(this.experience);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.experience = input.readDouble();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public double getExperience() {
        return this.experience;
    }

    @Override
    public void setExperience(double experience) {
        this.experience = experience;
    }

    @Override
    public double addExperience(double experience) {
        return this.runAction(() -> this.experience += experience);
    }

    @Override
    public void removeExperience(double experience) {
        this.runAction(() -> this.experience -= experience);
    }

    private double runAction(Runnable action) {
        final int oldLevel = this.getLevel();
        final double oldExperience = this.experience;

        action.run();

        final int newLevel = this.getLevel();
        final IHyriEventBus eventBus = HyriAPI.get().getNetworkManager().getEventBus();

        if (oldExperience != this.experience) {
            eventBus.publish(new GuildXPEvent(this.guild.getId(), this.getName(), oldExperience, this.experience));
        }

        if (newLevel > oldLevel) {
            eventBus.publish(new GuildLevelEvent(this.guild.getId(), this.getName(), oldLevel, newLevel));
        }
        return this.experience - oldExperience;
    }

    @Override
    public int getLevel() {
        return ALGORITHM.experienceToLevel(this.experience);
    }

    @Override
    public Algorithm getAlgorithm() {
        return ALGORITHM;
    }

}
