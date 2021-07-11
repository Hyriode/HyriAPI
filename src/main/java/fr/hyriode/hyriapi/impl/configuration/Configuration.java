package fr.hyriode.hyriapi.impl.configuration;

import fr.hyriode.hyriapi.impl.configuration.nested.RedisConfiguration;
import fr.hyriode.hyriapi.impl.configuration.nested.SpawnConfiguration;

public class Configuration {

    private RedisConfiguration redisConfiguration;
    private SpawnConfiguration spawnConfiguration;

    public Configuration(RedisConfiguration redisConfiguration, SpawnConfiguration spawnConfiguration) {
        this.redisConfiguration = redisConfiguration;
        this.spawnConfiguration = spawnConfiguration;
    }

    public SpawnConfiguration getSpawnConfiguration() {
        return this.spawnConfiguration;
    }

    public void setSpawnConfiguration(SpawnConfiguration spawnConfiguration) {
        this.spawnConfiguration = spawnConfiguration;
    }

    public RedisConfiguration getRedisConfiguration() {
        return this.redisConfiguration;
    }

    public void setRedisConfiguration(RedisConfiguration redisConfiguration) {
        this.redisConfiguration = redisConfiguration;
    }

}
