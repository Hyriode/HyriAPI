package fr.hyriode.api.impl.server.configuration;

import fr.hyriode.api.configuration.HydrionConfig;
import fr.hyriode.api.configuration.HyriRedisConfig;
import fr.hyriode.api.configuration.IHyriAPIConfiguration;
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
public class HyriAPIConfiguration implements IHyriAPIConfiguration {

    private final boolean devEnvironment;
    private final boolean production;
    private final boolean hyggdrasil;
    private final HyriRedisConfig redisConfiguration;
    private final HydrionConfig hydrionConfig;

    public HyriAPIConfiguration(boolean devEnvironment, boolean production, boolean hyggdrasil, HyriRedisConfig redisConfiguration, HydrionConfig hydrionConfig) {
        this.devEnvironment = devEnvironment;
        this.production = production;
        this.hyggdrasil = hyggdrasil;
        this.redisConfiguration = redisConfiguration;
        this.hydrionConfig = hydrionConfig;
    }

    @Override
    public boolean isDevEnvironment() {
        return this.devEnvironment;
    }

    @Override
    public boolean isProduction() {
        return this.production;
    }

    @Override
    public boolean withHyggdrasil() {
        return this.hyggdrasil;
    }

    @Override
    public HyriRedisConfig getRedisConfig() {
        return this.redisConfiguration;
    }

    @Override
    public HydrionConfig getHydrionConfig() {
        return this.hydrionConfig;
    }

    public static class Loader {

        private static final String DEV_ENVIRONMENT_PATH = "devEnvironment";
        private static final 
        private static final String HYGGDRASIL_PATH = "hyggdrasil";
        private static final String REDIS_PATH = "redis";
        private static final String HYDRION_PATH = "hydrion";

        private static final String REDIS_HOSTNAME = "hostname";
        private static final String REDIS_PORT = "port";
        private static final String REDIS_PASSWORD = "password";

        private static final String HYDRION_ENABLED = "enabled";
        private static final String HYDRION_URL = "url";
        private static final String HYDRION_API_KEY = "apiKey";

        public static IHyriAPIConfiguration load(JavaPlugin plugin) {
            final FileConfiguration config = plugin.getConfig();

            create(plugin);

            return new HyriAPIConfiguration(config.getBoolean(DEV_ENVIRONMENT_PATH), production, config.getBoolean(HYGGDRASIL_PATH),
                    loadRedisConfiguration(config.getConfigurationSection(REDIS_PATH)), loadHydrionConfiguration(config.getConfigurationSection(HYDRION_PATH)));
        }

        private static HyriRedisConfig loadRedisConfiguration(ConfigurationSection section) {
            final String hostname = section.getString(REDIS_HOSTNAME);
            final int port = section.getInt(REDIS_PORT);
            final String password = section.getString(REDIS_PASSWORD);

            return new HyriRedisConfig(hostname, port, password);
        }

        private static HydrionConfig loadHydrionConfiguration(ConfigurationSection section) {
            final boolean enabled = section.getBoolean(HYDRION_ENABLED);
            final String url = section.getString(HYDRION_URL);
            final UUID apiKey = UUID.fromString(section.getString(HYDRION_API_KEY));

            return new HydrionConfig(enabled, url, apiKey);
        }

        private static void create(JavaPlugin plugin) {
            if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
                final FileConfiguration config = plugin.getConfig();

                config.set(DEV_ENVIRONMENT_PATH, true);
                config.set(HYGGDRASIL_PATH, true);

                createRedisConfiguration(config.createSection(REDIS_PATH));
                createHydrionConfiguration(config.createSection(HYDRION_PATH));

                plugin.saveConfig();
            }
        }

        private static void createRedisConfiguration(ConfigurationSection section) {
            section.set(REDIS_HOSTNAME, "127.0.0.1");
            section.set(REDIS_PORT, 6379);
            section.set(REDIS_PASSWORD, "");
        }

        private static void createHydrionConfiguration(ConfigurationSection section) {
            section.set(HYDRION_ENABLED, true);
            section.set(HYDRION_URL, "https://hydrion.hyriode.fr");
            section.set(HYDRION_API_KEY, UUID.randomUUID().toString());
        }

    }

}
