package fr.hyriode.api.impl.proxy.config;

import fr.hyriode.api.config.MongoDBConfig;
import fr.hyriode.api.config.RedisConfig;
import fr.hyriode.api.config.IHyriAPIConfig;
import fr.hyriode.api.impl.proxy.HyriAPIPlugin;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/11/2021 at 15:12
 */
public class HyriAPIConfig implements IHyriAPIConfig {

    private final boolean devEnvironment;
    private final boolean hyggdrasil;
    private final RedisConfig redisConfig;
    private final MongoDBConfig mongoDBConfig;
    private final String serverIcon;
    private final int slots;
    private final String motd;

    public HyriAPIConfig(boolean devEnvironment, boolean hyggdrasil, RedisConfig redisConfig, MongoDBConfig mongoDBConfig, String serverIcon, int slots, String motd) {
        this.devEnvironment = devEnvironment;
        this.hyggdrasil = hyggdrasil;
        this.redisConfig = redisConfig;
        this.mongoDBConfig = mongoDBConfig;
        this.serverIcon = serverIcon;
        this.slots = slots;
        this.motd = motd;
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

    public String getServerIcon() {
        return this.serverIcon;
    }

    public int getSlots() {
        return this.slots;
    }

    public String getMotd() {
        return this.motd;
    }

    public static class Loader {

        private static final String DEV_ENVIRONMENT_PATH = "devEnvironment";
        private static final String HYGGDRASIL_PATH = "hyggdrasil";
        private static final String REDIS_PATH = "redis";
        private static final String MONGODB_PATH = "mongodb";
        private static final String SERVER_ICON_PATH = "server-icon";
        private static final String SLOTS_PATH = "slots";
        private static final String MOTD_PATH = "motd";

        private static final String REDIS_HOSTNAME = "hostname";
        private static final String REDIS_PORT = "port";
        private static final String REDIS_PASSWORD = "password";

        private static final String MONGODB_USERNAME = "username";
        private static final String MONGODB_PASSWORD = "password";
        private static final String MONGODB_HOSTNAME = "hostname";
        private static final String MONGODB_PORT = "port";

        public static HyriAPIConfig load(Plugin plugin) {
            try {
                final File dataFolder = plugin.getDataFolder();
                final File configurationFile = new File(dataFolder, "config.yml");

                if (!dataFolder.exists()) {
                    dataFolder.mkdir();
                }

                if (!configurationFile.exists()) {
                    final InputStream inputStream = HyriAPIPlugin.class.getResourceAsStream("config.yml");

                    if (inputStream != null) {
                        Files.copy(inputStream, configurationFile.toPath());
                    }
                }

                final Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configurationFile);

                return new HyriAPIConfig(config.getBoolean(DEV_ENVIRONMENT_PATH), config.getBoolean(HYGGDRASIL_PATH),
                        loadRedisConfig(config.getSection(REDIS_PATH)), loadMongoDBConfig(config.getSection(MONGODB_PATH)),
                        config.getString(SERVER_ICON_PATH), config.getInt(SLOTS_PATH), config.getString(MOTD_PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static RedisConfig loadRedisConfig(Configuration section) {
            final String hostname = section.getString(REDIS_HOSTNAME);
            final int port = section.getInt(REDIS_PORT);
            final String password = section.getString(REDIS_PASSWORD);

            return new RedisConfig(hostname, port, password);
        }

        private static MongoDBConfig loadMongoDBConfig(Configuration section) {
            final String username = section.getString(MONGODB_USERNAME);
            final String password = section.getString(MONGODB_PASSWORD);
            final String hostname = section.getString(MONGODB_HOSTNAME);
            final int port = section.getInt(MONGODB_PORT);

            return new MongoDBConfig(username, password, hostname, port);
        }

    }

}
