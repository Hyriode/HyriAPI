package fr.hyriode.api.hyggdrasil;

import fr.hyriode.hyggdrasil.api.HyggdrasilAPI;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggEnvironment;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 20/05/2022 at 07:10
 */
public interface IHyggdrasilManager {

    /**
     * Check if Hyggdrasil is ued by the server/proxy
     *
     * @return <code>true</code> if it is
     */
    boolean withHyggdrasil();

    /**
     * Get the Hyggdrasil API instance
     *
     * @return The {@link HyggdrasilAPI} instance
     */
    HyggdrasilAPI getHyggdrasilAPI();

    /**
     * Get the environment provided by Hyggdrasil before starting the server/proxy
     *
     * @return A {@link HyggEnvironment} object
     */
    HyggEnvironment getEnvironment();

    /**
     * Get the application object of the current server/proxy
     *
     * @return A {@link HyggApplication} object
     */
    HyggApplication getApplication();

    /**
     * Send data of the application to Hyggdrasil.<br>
     * Warning: this will only work if the application supports this system!
     */
    void sendData();

}
