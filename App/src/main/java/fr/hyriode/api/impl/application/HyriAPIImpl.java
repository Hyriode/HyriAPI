package fr.hyriode.api.impl.application;

import fr.hyriode.api.impl.application.config.HyriAPIConfig;
import fr.hyriode.api.impl.common.CHyriAPIImpl;
import fr.hyriode.api.server.join.IHyriJoinManager;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggEnv;

import java.util.logging.Level;

/**
 * Created by AstFaster
 * on 09/07/2022 at 17:58
 */
public class HyriAPIImpl extends CHyriAPIImpl {

    public HyriAPIImpl(HyriAPIConfig config, String applicationName) {
        super(config);

        this.log("Starting HyriAPI for '" + applicationName + "'.");

        this.preInit();
        this.init(new HyggEnv(new HyggApplication(HyggApplication.Type.OTHER, applicationName, System.currentTimeMillis())), null);
        this.postInit();
    }

    @Override
    public void log(Level level, String message) {
        if (level == Level.SEVERE) {
            System.err.println(message);
        } else {
            System.out.println(message);
        }
    }

    @Override
    public IHyriJoinManager getJoinManager() {
        return null;
    }

}
