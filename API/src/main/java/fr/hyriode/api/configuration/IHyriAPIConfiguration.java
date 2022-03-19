package fr.hyriode.api.configuration;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 12/02/2022 at 16:04
 */
public interface IHyriAPIConfiguration {

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
     * Get the Redis configuration for Redis
     *
     * @return The {@link HyriRedisConfiguration} object
     */
    HyriRedisConfiguration getRedisConfiguration();

}
