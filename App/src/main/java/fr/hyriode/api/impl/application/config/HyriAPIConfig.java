package fr.hyriode.api.impl.application.config;

import fr.hyriode.api.config.MongoDBConfig;
import fr.hyriode.api.config.RedisConfig;
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
    private final RedisConfig redisConfig;
    private final MongoDBConfig mongoDBConfig;

    public HyriAPIConfig(boolean devEnvironment, boolean hyggdrasil, RedisConfig redisConfig, MongoDBConfig mongoDBConfig) {
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
    public RedisConfig getRedisConfig() {
        return this.redisConfig;
    }

    @Override
    public MongoDBConfig getMongoDBConfig() {
        return this.mongoDBConfig;
    }

    public static class Builder implements IBuilder<HyriAPIConfig> {

        private final BuilderEntry<Boolean> devEnvironment = new BuilderEntry<>("Development environment", () -> true).required();
        private final BuilderEntry<Boolean> hyggdrasil = new BuilderEntry<>("Hyggdrasil", () -> false).required();
        private final BuilderEntry<RedisConfig> redisConfig = new BuilderEntry<>("Redis configuration", () -> new RedisConfig("127.0.0.1", 6379, "")).required();
        private final BuilderEntry<MongoDBConfig> mongoDBConfig = new BuilderEntry<>("MongoDB configuration", () -> new MongoDBConfig(null, null, "127.0.0.1", 27017)).required();

        public Builder withDevEnvironment(boolean devEnvironment) {
            this.devEnvironment.set(() -> devEnvironment);
            return this;
        }

        public Builder withHyggdrasil(boolean hyggdrasil) {
            this.hyggdrasil.set(() -> hyggdrasil);
            return this;
        }

        public Builder withRedisConfig(RedisConfig redisConfig) {
            this.redisConfig.set(() -> redisConfig);
            return this;
        }

        public Builder withMongoDBConfig(MongoDBConfig mongoDBConfig) {
            this.mongoDBConfig.set(() -> mongoDBConfig);
            return this;
        }

        @Override
        public HyriAPIConfig build() {
            return new HyriAPIConfig(this.devEnvironment.get(), this.hyggdrasil.get(), this.redisConfig.get(), this.mongoDBConfig.get());
        }

    }

}
