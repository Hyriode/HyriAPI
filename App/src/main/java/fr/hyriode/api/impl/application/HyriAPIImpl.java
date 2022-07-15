package fr.hyriode.api.impl.application;

import fr.hyriode.api.impl.application.config.HyriAPIConfig;
import fr.hyriode.api.impl.common.HyriCommonImplementation;

import java.util.logging.Level;

/**
 * Created by AstFaster
 * on 09/07/2022 at 17:58
 */
public class HyriAPIImpl extends HyriCommonImplementation {

    public HyriAPIImpl(HyriAPIConfig config) {
        super(config, null, (level, message) -> {
            if (level == Level.SEVERE) {
                System.err.println(message);
            } else {
                System.out.println(message);
            }
        });
    }

}
