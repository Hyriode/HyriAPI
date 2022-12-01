package fr.hyriode.api.config;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 12/02/2022 at 16:04
 */
public interface IHyriAPIConfig {

    /**
     * Check if HyriAPI is running on development environment
     *
     * @return <code>true</code> if it's development environment
     */
    boolean isDevEnvironment();

    /**
     * Check if HyriAPI is currently using Hyggdrasil
     *
     * @return <code>true</code> if it's using Hyggdrasil
     */
    boolean withHyggdrasil();

    /**
     * Get the Redis configuration
     *
     * @return The {@link RedisConfig} object
     */
    RedisConfig getRedisConfig();

    /**
     * Get the MongoDB configuration
     *
     * @return The {@linkplain MongoDBConfig MongoDB config} object
     */
    MongoDBConfig getMongoDBConfig();

}
