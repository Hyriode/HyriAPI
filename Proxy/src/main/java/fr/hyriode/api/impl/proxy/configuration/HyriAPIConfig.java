package fr.hyriode.api.impl.proxy.configuration;

import fr.hyriode.api.configuration.HydrionConfig;
import fr.hyriode.api.configuration.HyriRedisConfig;
import fr.hyriode.api.configuration.IHyriAPIConfiguration;
import fr.hyriode.api.impl.proxy.HyriAPIPlugin;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/11/2021 at 15:12
 */
public class HyriAPIConfiguration implements IHyriAPIConfiguration {

    private final boolean devEnvironment;
    private final boolean hyggdrasil;
    private final boolean production;
    private final HyriRedisConfig redisConfiguration;
    private final HydrionConfig hydrionConfig;
    private final String serverIcon;
    private final int slots;
    private final String motd;

    public HyriAPIConfiguration(boolean devEnvironment, boolean hyggdrasil, boolean production, HyriRedisConfig redisConfiguration, HydrionConfig hydrionConfig, String serverIcon, int slots, String motd) {
        this.devEnvironment = devEnvironment;
        this.hyggdrasil = hyggdrasil;
        this.production = production;
        this.redisConfiguration = redisConfiguration;
        this.hydrionConfig = hydrionConfig;
        this.serverIcon = serverIcon;
        this.slots = slots;
        this.motd = motd;
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
        private static final String PRODUCTION = "production";
        private static final String HYGGDRASIL_PATH = "hyggdrasil";
        private static final String REDIS_PATH = "redis";
        private static final String HYDRION_PATH = "hydrion";
        private static final String SERVER_ICON_PATH = "server-icon";
        private static final String SLOTS_PATH = "slots";
        private static final String MOTD_PATH = "motd";

        private static final String REDIS_HOSTNAME = "hostname";
        private static final String REDIS_PORT = "port";
        private static final String REDIS_PASSWORD = "password";

        private static final String HYDRION_ENABLED = "enabled";
        private static final String HYDRION_URL = "url";
        private static final String HYDRION_API_KEY = "apiKey";

        public static HyriAPIConfiguration load(Plugin plugin) {
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

                return new HyriAPIConfiguration(config.getBoolean(DEV_ENVIRONMENT_PATH), config.getBoolean(HYGGDRASIL_PATH),
                        config.getBoolean(PRODUCTION), loadRedisConfiguration(config.getSection(REDIS_PATH)), loadHydrionConfiguration(config.getSection(HYDRION_PATH)),
                        config.getString(SERVER_ICON_PATH), config.getInt(SLOTS_PATH), config.getString(MOTD_PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static HyriRedisConfig loadRedisConfiguration(Configuration section) {
            final String hostname = section.getString(REDIS_HOSTNAME);
            final int port = section.getInt(REDIS_PORT);
            final String password = section.getString(REDIS_PASSWORD);

            return new HyriRedisConfig(hostname, port, password);
        }

        private static HydrionConfig loadHydrionConfiguration(Configuration section) {
            final boolean enabled = section.getBoolean(HYDRION_ENABLED);
            final String url = section.getString(HYDRION_URL);
            final UUID apiKey = UUID.fromString(section.getString(HYDRION_API_KEY));

            return new HydrionConfig(enabled, url, apiKey);
        }

    }

}
