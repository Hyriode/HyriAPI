package fr.hyriode.hyriapi.impl.configuration;

import fr.hyriode.hyriapi.impl.configuration.nested.RedisConfiguration;

public class Configuration {

    private RedisConfiguration redisConfiguration;

    public Configuration(RedisConfiguration redisConfiguration) {
        this.redisConfiguration = redisConfiguration;
    }

    public RedisConfiguration getRedisConfiguration() {
        return this.redisConfiguration;
    }

    public void setRedisConfiguration(RedisConfiguration redisConfiguration) {
        this.redisConfiguration = redisConfiguration;
    }

}
