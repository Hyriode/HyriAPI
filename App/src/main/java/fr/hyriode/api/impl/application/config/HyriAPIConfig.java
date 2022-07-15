package fr.hyriode.api.impl.application.config;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.config.HyriMongoDBConfig;
import fr.hyriode.api.config.HyriRedisConfig;
import fr.hyriode.api.config.IHyriAPIConfig;
import fr.hyriode.hyggdrasil.api.util.builder.BuilderEntry;
import fr.hyriode.hyggdrasil.api.util.builder.IBuilder;

/**
 * Created by AstFaster
 * on 09/07/2022 at 17:54
 */
public class HyriAPIConfig implements IHyriAPIConfig {

    private final boolean devEnvironment;
    private final boolean hyggdrasil;
    private final HyriRedisConfig redisConfig;
    private final HyriMongoDBConfig mongoDBConfig;

    public HyriAPIConfig(boolean devEnvironment, boolean hyggdrasil, HyriRedisConfig redisConfig, HyriMongoDBConfig mongoDBConfig) {
        this.devEnvironment = devEnvironment;
        this.hyggdrasil = hyggdrasil;
        this.redisConfig = redisConfig;
        this.mongoDBConfig = mongoDBConfig;
    }

    @Override
    public boolean isDevEnvironment() {
        return this.devEnvironment;
    }

    @Override
    public boolean withHyggdrasil() {
        return this.hyggdrasil;
    }

    @Override
    public HyriRedisConfig getRedisConfig() {
        return this.redisConfig;
    }

    @Override
    public HyriMongoDBConfig getMongoDBConfig() {
        return this.mongoDBConfig;
    }

    public static class Builder implements IBuilder<HyriAPIConfig> {

        private final BuilderEntry<Boolean> devEnvironment = new BuilderEntry<>("Development environment", () -> true).required();
        private final BuilderEntry<Boolean> hyggdrasil = new BuilderEntry<>("Hyggdrasil", () -> false).required();
        private final BuilderEntry<HyriRedisConfig> redisConfig = new BuilderEntry<>("Redis configuration", () -> new HyriRedisConfig("127.0.0.1", 6379, "")).required();
        private final BuilderEntry<HyriMongoDBConfig> mongoDBConfig = new BuilderEntry<>("MongoDB configuration", () -> new HyriMongoDBConfig(null, null, "127.0.0.1", 27017)).required();

        public Builder withDevEnvironment(boolean devEnvironment) {
            this.devEnvironment.set(() -> devEnvironment);
            return this;
        }

        public Builder withHyggdrasil(boolean hyggdrasil) {
            this.hyggdrasil.set(() -> hyggdrasil);
            return this;
        }

        public Builder withRedisConfig(HyriRedisConfig redisConfig) {
            this.redisConfig.set(() -> redisConfig);
            return this;
        }

        public Builder withMongoDBConfig(HyriMongoDBConfig mongoDBConfig) {
            this.mongoDBConfig.set(() -> mongoDBConfig);
            return this;
        }

        @Override
        public HyriAPIConfig build() {
            return new HyriAPIConfig(this.devEnvironment.get(), this.hyggdrasil.get(), this.redisConfig.get(), this.mongoDBConfig.get());
        }

    }

}
