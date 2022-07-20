package fr.hyriode.api.impl.application;

import fr.hyriode.api.impl.application.config.HyriAPIConfig;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggData;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggEnvironment;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggKeys;

import java.util.logging.Level;

/**
 * Created by AstFaster
 * on 09/07/2022 at 17:58
 */
public class HyriAPIImpl extends HyriCommonImplementation {

    public HyriAPIImpl(HyriAPIConfig config, String applicationName) {
        super(config, null, (level, message) -> {
            if (level == Level.SEVERE) {
                System.err.println(message);
            } else {
                System.out.println(message);
            }
        }, new HyggEnvironment(new HyggApplication(HyggApplication.Type.OTHER, applicationName, System.currentTimeMillis()), new HyggKeys(null, null), new HyggData()));

        log("Starting HyriAPI for '" + applicationName + "'.");
    }

}
