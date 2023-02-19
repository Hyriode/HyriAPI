package fr.hyriode.api.leveling;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.leaderboard.IHyriLeaderboard;
import fr.hyriode.api.leveling.event.NetworkLevelEvent;
import fr.hyriode.api.leveling.event.NetworkXPEvent;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 15/04/2022 at 12:42
 */
public class NetworkLeveling implements IHyriLeveling, MongoSerializable, DataSerializable {

    public static final String LEADERBOARD_TYPE = "network-leveling";
    public static final String LEADERBOARD_NAME = "experience";

    public static final String NAME = "network";

    public static final Supplier<IHyriLeaderboard> LEADERBOARD = () -> HyriAPI.get().getLeaderboardProvider().getLeaderboard(LEADERBOARD_TYPE, LEADERBOARD_NAME);
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
    @Expose
    private Set<Integer> claimedRewards = new HashSet<>();

    private final IHyriPlayer player;

    public NetworkLeveling(IHyriPlayer player) {
        this.player = player;
    }

    @Override
    public void save(MongoDocument document) {
        document.append("experience", this.experience);
        document.append("claimed_rewards", this.claimedRewards);
    }

    @Override
    public void load(MongoDocument document) {
        this.experience = document.getDouble("experience");
        this.claimedRewards.addAll(document.getList("claimed_rewards", Integer.class));
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeDouble(this.experience);
        output.writeInt(this.claimedRewards.size());

        for (int claimedReward : this.claimedRewards) {
            output.writeInt(claimedReward);
        }
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.experience = input.readDouble();

        final int size = input.readInt();

        for (int i = 0; i < size; i++) {
            this.claimedRewards.add(input.readInt());
        }
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
        this.runAction(() -> this.experience = experience);
    }

    @Override
    public double addExperience(double experience) {
        return this.addExperience(experience, true);
    }

    public double addExperience(double experience, boolean multipliers) {
        return this.runAction(() -> this.experience += multipliers ? this.applyMultiplier(experience) : experience);
    }

    @Override
    public void removeExperience(double experience) {
        this.runAction(() -> this.experience -= experience);
    }

    @Override
    public int getLevel() {
        return ALGORITHM.experienceToLevel(this.experience);
    }

    @Override
    public Algorithm getAlgorithm() {
        return ALGORITHM;
    }

    private double runAction(Runnable action) {
        final int oldLevel = this.getLevel();
        final double oldExperience = this.experience;

        action.run();

        final int newLevel = this.getLevel();
        final IHyriEventBus eventBus = HyriAPI.get().getNetworkManager().getEventBus();

        if (oldExperience != this.experience) {
            LEADERBOARD.get().setScore(this.player.getUniqueId(), this.experience);

            eventBus.publish(new NetworkXPEvent(this.player.getUniqueId(), this.getName(), oldExperience, this.experience));
        }

        if (newLevel > oldLevel) {
            eventBus.publish(new NetworkLevelEvent(this.player.getUniqueId(), this.getName(), oldLevel, newLevel));
        }
        return this.experience - oldExperience;
    }

    @Override
    public double applyMultiplier(double experience) {
        final Multiplier multiplier = Multiplier.getByPlayer(this.player);

        if (multiplier != null) {
            return experience * multiplier.getAmount();
        }
        return experience;
    }

    public void claimReward(int level) {
        this.claimedRewards.add(level);
    }

    public Set<Integer> getClaimedRewards() {
        return this.claimedRewards == null ? this.claimedRewards = new HashSet<>() : this.claimedRewards;
    }

    public enum Multiplier {

        PLAYER(PlayerRank.PLAYER, 1.0D),
        VIP(PlayerRank.VIP, 1.50D),
        VIP_PLUS(PlayerRank.VIP_PLUS, 1.75D),
        EPIC(PlayerRank.EPIC, 2.0D),
        HYRI_PLUS(PlayerRank.EPIC, 2.50D);

        private final PlayerRank rank;
        private final double amount;

        Multiplier(PlayerRank rank, double amount) {
            this.rank = rank;
            this.amount = amount;
        }

        public PlayerRank getRank() {
            return this.rank;
        }

        public double getAmount() {
            return this.amount;
        }

        public static Multiplier getByPlayer(IHyriPlayer account) {
            if (account.getHyriPlus().has()) {
                return HYRI_PLUS;
            }

            for (Multiplier multiplier : values()) {
                if (multiplier.getRank() == account.getRank().getPlayerType()) {
                    return multiplier;
                }
            }
            return null;
        }

    }

}
