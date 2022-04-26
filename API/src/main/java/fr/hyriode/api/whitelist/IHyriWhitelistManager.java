package fr.hyriode.api.whitelist;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/04/2022 at 07:05
 */
public interface IHyriWhitelistManager {

    void whitelistPlayer(String name, int seconds);

    default void whitelistPlayer(String name) {
        this.whitelistPlayer(name, 2 * 60);
    }

    void removePlayerFromWhitelist(String name);

    boolean isWhitelisted(String name);

}
