package fr.hyriode.api.impl.server.config;

import fr.hyriode.api.config.HyriMongoDBConfig;
import fr.hyriode.api.config.HyriRedisConfig;
import fr.hyriode.api.config.IHyriAPIConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/11/2021 at 15:12
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

    public static class Loader {

        private static final String DEV_ENVIRONMENT_PATH = "devEnvironment";
        private static final String HYGGDRASIL_PATH = "hyggdrasil";
        private static final String REDIS_PATH = "redis";
        private static final String MONGODB_PATH = "mongodb";

        private static final String REDIS_HOSTNAME = "hostname";
        private static final String REDIS_PORT = "port";
        private static final String REDIS_PASSWORD = "password";

        private static final String MONGODB_USERNAME = "username";
        private static final String MONGODB_PASSWORD = "password";
        private static final String MONGODB_HOSTNAME = "hostname";
        private static final String MONGODB_PORT = "port";

        public static IHyriAPIConfig load(JavaPlugin plugin) {
            final FileConfiguration config = plugin.getConfig();

            create(plugin);

            return new HyriAPIConfig(config.getBoolean(DEV_ENVIRONMENT_PATH), config.getBoolean(HYGGDRASIL_PATH),
                    loadRedisConfig(config.getConfigurationSection(REDIS_PATH)), loadMongoDBConfig(config.getConfigurationSection(MONGODB_PATH)));
        }

        private static HyriRedisConfig loadRedisConfig(ConfigurationSection section) {
            final String hostname = section.getString(REDIS_HOSTNAME);
            final int port = section.getInt(REDIS_PORT);
            final String password = section.getString(REDIS_PASSWORD);

            return new HyriRedisConfig(hostname, port, password);
        }

        private static HyriMongoDBConfig loadMongoDBConfig(ConfigurationSection section) {
            final String username = section.getString(MONGODB_USERNAME);
            final String password = section.getString(MONGODB_PASSWORD);
            final String hostname = section.getString(MONGODB_HOSTNAME);
            final int port = section.getInt(MONGODB_PORT);

            return new HyriMongoDBConfig(username, password, hostname, port);
        }

        private static void create(JavaPlugin plugin) {
            if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
                final FileConfiguration config = plugin.getConfig();

                config.set(DEV_ENVIRONMENT_PATH, true);
                config.set(HYGGDRASIL_PATH, true);

                createRedisConfig(config.createSection(REDIS_PATH));
                createMongoDBConfig(config.createSection(MONGODB_PATH));

                plugin.saveConfig();
            }
        }

        private static void createRedisConfig(ConfigurationSection section) {
            section.set(REDIS_HOSTNAME, "127.0.0.1");
            section.set(REDIS_PORT, 6379);
            section.set(REDIS_PASSWORD, "");
        }

        private static void createMongoDBConfig(ConfigurationSection section) {
            section.set(MONGODB_USERNAME, "username");
            section.set(MONGODB_PASSWORD, "password");
            section.set(MONGODB_HOSTNAME, "127.0.0.1");
            section.set(MONGODB_PORT, 27017);
        }

    }

}
