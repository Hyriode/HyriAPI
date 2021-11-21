package fr.hyriode.hyriapi.impl.configuration;

import fr.hyriode.hyriapi.impl.configuration.nested.RedisConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/11/2021 at 15:12
 */
public class HyriAPIConfiguration {

    private boolean devEnvironment;
    private RedisConfiguration redisConfiguration;

    public HyriAPIConfiguration(boolean devEnvironment, RedisConfiguration redisConfiguration) {
        this.devEnvironment = devEnvironment;
        this.redisConfiguration = redisConfiguration;
    }

    public boolean isDevEnvironment() {
        return this.devEnvironment;
    }

    public void setDevEnvironment(boolean devEnvironment) {
        this.devEnvironment = devEnvironment;
    }

    public RedisConfiguration getRedisConfiguration() {
        return this.redisConfiguration;
    }

    public void setRedisConfiguration(RedisConfiguration redisConfiguration) {
        this.redisConfiguration = redisConfiguration;
    }

    public static class Loader {

        private static final String DEV_ENVIRONMENT_PATH = "devEnvironment";
        private static final String REDIS_PATH = "redis";

        public static HyriAPIConfiguration load(JavaPlugin plugin) {
            final FileConfiguration fileConfiguration = plugin.getConfig();

            create(plugin);

            return new HyriAPIConfiguration(fileConfiguration.getBoolean(DEV_ENVIRONMENT_PATH), RedisConfiguration.build(fileConfiguration.getConfigurationSection(REDIS_PATH)));
        }

        private static void create(JavaPlugin plugin) {
            if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
                final FileConfiguration fileConfiguration = plugin.getConfig();

                fileConfiguration.set(DEV_ENVIRONMENT_PATH, true);

                RedisConfiguration.setDefault(fileConfiguration.createSection(REDIS_PATH));

                plugin.saveConfig();
            }
        }
    }

}
