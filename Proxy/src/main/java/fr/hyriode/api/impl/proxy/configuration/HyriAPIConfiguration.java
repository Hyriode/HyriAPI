package fr.hyriode.api.impl.proxy.configuration;

import fr.hyriode.api.configuration.HyriRedisConfiguration;
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

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/11/2021 at 15:12
 */
public class HyriAPIConfiguration implements IHyriAPIConfiguration {

    private final boolean devEnvironment;
    private final boolean hyggdrasil;
    private final HyriRedisConfiguration redisConfiguration;
    private final String serverIcon;
    private final int slots;

    public HyriAPIConfiguration(boolean devEnvironment, boolean hyggdrasil, HyriRedisConfiguration redisConfiguration, String serverIcon, int slots) {
        this.devEnvironment = devEnvironment;
        this.hyggdrasil = hyggdrasil;
        this.redisConfiguration = redisConfiguration;
        this.serverIcon = serverIcon;
        this.slots = slots;
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

    public String getServerIcon() {
        return this.serverIcon;
    }

    public int getSlots() {
        return this.slots;
    }

    public static class Loader {

        private static final String DEV_ENVIRONMENT_PATH = "devEnvironment";
        private static final String HYGGDRASIL_PATH = "hyggdrasil";
        private static final String REDIS_PATH = "redis";
        private static final String SERVER_ICON_PATH = "server-icon";
        private static final String SLOTS_PATH = "slots";

        private static final String REDIS_HOSTNAME = "hostname";
        private static final String REDIS_PORT = "port";
        private static final String REDIS_PASSWORD = "password";

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

                final Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configurationFile);

                return new HyriAPIConfiguration(configuration.getBoolean(DEV_ENVIRONMENT_PATH), configuration.getBoolean(HYGGDRASIL_PATH), loadRedisConfiguration(configuration.getSection(REDIS_PATH)), configuration.getString(SERVER_ICON_PATH), configuration.getInt(SLOTS_PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static HyriRedisConfiguration loadRedisConfiguration(Configuration section) {
            final String hostname = section.getString(REDIS_HOSTNAME);
            final int port = section.getInt(REDIS_PORT);
            final String password = section.getString(REDIS_PASSWORD);

            return new HyriRedisConfiguration(hostname, port, password);
        }

    }

}
