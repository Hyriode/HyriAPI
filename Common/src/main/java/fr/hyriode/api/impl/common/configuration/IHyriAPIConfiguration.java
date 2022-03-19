package fr.hyriode.api.impl.common.configuration;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 12/02/2022 at 16:04
 */
public interface IHyriAPIConfiguration {

    boolean isDevEnvironment();

    boolean withHyggdrasil();

    HyriRedisConfiguration getRedisConfiguration();

}
