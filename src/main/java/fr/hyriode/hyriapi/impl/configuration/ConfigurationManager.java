package fr.hyriode.hyriapi.impl.configuration;

import fr.hyriode.hyriapi.impl.configuration.nested.RedisConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigurationManager {

    private final String redisPath = "redis";

    private Configuration configuration;

    private final FileConfiguration fileConfiguration;

    private final JavaPlugin plugin;

    public ConfigurationManager(JavaPlugin plugin, boolean load) {
        this.plugin = plugin;
        this.fileConfiguration = this.plugin.getConfig();

        if (load) this.loadConfiguration();
    }

    public void loadConfiguration() {
        this.createConfiguration();

        final ConfigurationSection redisSection = this.fileConfiguration.getConfigurationSection(this.redisPath);
        final RedisConfiguration redisConfiguration = new RedisConfiguration(redisSection.getString("ip"), redisSection.getInt("port"), redisSection.getString("password"));

        this.configuration = new Configuration(redisConfiguration);
    }

    private void createConfiguration() {
        if (!new File(this.plugin.getDataFolder(), "config.yml").exists()) {
            this.fileConfiguration.createSection(this.redisPath);

            final ConfigurationSection redisSection = this.fileConfiguration.getConfigurationSection(this.redisPath);

            redisSection.set("ip", "127.0.0.1");
            redisSection.set("port", 6379);
            redisSection.set("password", "");

            this.plugin.saveConfig();
        }
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

}
