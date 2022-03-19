package fr.hyriode.api.impl.server.configuration;

import fr.hyriode.api.configuration.HyriRedisConfiguration;
import fr.hyriode.api.configuration.IHyriAPIConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/11/2021 at 15:12
 */
public class HyriAPIConfiguration implements IHyriAPIConfiguration {

    private final boolean devEnvironment;
    private final boolean hyggdrasil;
    private final HyriRedisConfiguration redisConfiguration;

    public HyriAPIConfiguration(boolean devEnvironment, boolean hyggdrasil, HyriRedisConfiguration redisConfiguration) {
        this.devEnvironment = devEnvironment;
        this.hyggdrasil = hyggdrasil;
        this.redisConfiguration = redisConfiguration;
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
    public HyriRedisConfiguration getRedisConfiguration() {
        return this.redisConfiguration;
    }

    public static class Loader {

        private static final String DEV_ENVIRONMENT_PATH = "devEnvironment";
        private static final String HYGGDRASIL_PATH = "hyggdrasil";
        private static final String REDIS_PATH = "redis";

        private static final String REDIS_HOSTNAME = "hostname";
        private static final String REDIS_PORT = "port";
        private static final String REDIS_PASSWORD = "password";

        public static IHyriAPIConfiguration load(JavaPlugin plugin) {
            final FileConfiguration config = plugin.getConfig();

            create(plugin);

            return new HyriAPIConfiguration(config.getBoolean(DEV_ENVIRONMENT_PATH), config.getBoolean(HYGGDRASIL_PATH), loadRedisConfiguration(config.getConfigurationSection(REDIS_PATH)));
        }

        private static HyriRedisConfiguration loadRedisConfiguration(ConfigurationSection section) {
            final String hostname = section.getString(REDIS_HOSTNAME);
            final int port = section.getInt(REDIS_PORT);
            final String password = section.getString(REDIS_PASSWORD);

            return new HyriRedisConfiguration(hostname, port, password);
        }

        private static void create(JavaPlugin plugin) {
            if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
                final FileConfiguration config = plugin.getConfig();

                config.set(DEV_ENVIRONMENT_PATH, true);
                config.set(HYGGDRASIL_PATH, true);

                createRedisConfiguration(config.createSection(REDIS_PATH));

                plugin.saveConfig();
            }
        }

        private static void createRedisConfiguration(ConfigurationSection section) {
            section.set(REDIS_HOSTNAME, "127.0.0.1");
            section.set(REDIS_PORT, 6379);
            section.set(REDIS_PASSWORD, "");
        }

    }

}
