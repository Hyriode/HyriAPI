package fr.hyriode.hyriapi.impl.configuration;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.configuration.nested.NPCConfiguration;
import fr.hyriode.hyriapi.impl.configuration.nested.RedisConfiguration;
import fr.hyriode.hyriapi.impl.configuration.nested.SpawnConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigurationManager {

    private final String redisPath = "redis";
    private final String spawnPath = "spawn";
    private final String npcPath = "npc";

    private Configuration configuration;

    private final FileConfiguration fileConfiguration;

    public ConfigurationManager(boolean load) {
        this.fileConfiguration = HyriAPI.get().getPlugin().getConfig();

        if (load) this.loadConfiguration();
    }

    public void loadConfiguration() {
        this.createConfiguration();

        final ConfigurationSection redisSection = this.fileConfiguration.getConfigurationSection(this.redisPath);
        final RedisConfiguration redisConfiguration = new RedisConfiguration(redisSection.getString("ip"), redisSection.getInt("port"), redisSection.getString("password"));

        final ConfigurationSection spawnSection = this.fileConfiguration.getConfigurationSection(this.spawnPath);
        final SpawnConfiguration spawnConfiguration = new SpawnConfiguration(spawnSection.getDouble("x"), spawnSection.getDouble("y"), spawnSection.getDouble("z"));

        this.configuration = new Configuration(redisConfiguration, spawnConfiguration);
    }

    private void createConfiguration() {
        if (!new File(HyriAPI.get().getPlugin().getDataFolder(), "config.yml").exists()) {
            this.fileConfiguration.createSection(this.redisPath);
            this.fileConfiguration.createSection(this.spawnPath);
            this.fileConfiguration.createSection(this.npcPath);

            final ConfigurationSection redisSection = this.fileConfiguration.getConfigurationSection(this.redisPath);

            redisSection.set("ip", "127.0.0.1");
            redisSection.set("port", 6379);
            redisSection.set("password", "");

            final ConfigurationSection spawnSection = this.fileConfiguration.getConfigurationSection(this.spawnPath);

            spawnSection.set("x", 0.0D);
            spawnSection.set("y", 0.0D);
            spawnSection.set("z", 0.0D);

            HyriAPI.get().getPlugin().saveConfig();
        }
    }

    public void addNPCConfiguration(String name, NPCConfiguration configuration) {
        if (this.getNPCSection().getConfigurationSection(name) == null) {
            final ConfigurationSection npcSection = this.getNPCSection().createSection(name);

            npcSection.set("world", configuration.getWorld());
            npcSection.set("x", configuration.getX());
            npcSection.set("y", configuration.getY());
            npcSection.set("z", configuration.getZ());
            npcSection.set("yaw", configuration.getYaw());
            npcSection.set("pitch", configuration.getPitch());
            npcSection.set("textureData", configuration.getTextureData());
            npcSection.set("textureSignature", configuration.getTextureSignature());

            HyriAPI.get().getPlugin().saveConfig();
        }
    }

    public NPCConfiguration getNPCConfiguration(String name) {
        final ConfigurationSection npcSection = this.getNPCSection().getConfigurationSection(name);

        return new NPCConfiguration(npcSection.getString("world"), npcSection.getDouble("x"), npcSection.getDouble("y"), npcSection.getDouble("z"), (float) npcSection.getDouble("yaw"), (float) npcSection.getDouble("pitch"), npcSection.getString("textureData"), npcSection.getString("textureSignature"));
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public ConfigurationSection getNPCSection() {
        return this.fileConfiguration.getConfigurationSection(this.npcPath);
    }

}
