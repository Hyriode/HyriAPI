package fr.hyriode.api.impl.common.hydrion;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.configuration.HydrionConfiguration;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.hydrion.client.HydrionClient;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 12:15
 */
public class HydrionManager {

    private HydrionClient client;

    private final HydrionConfiguration configuration;
    private final boolean enabled;

    public HydrionManager() {
        this.configuration = HyriAPI.get().getConfiguration().getHydrionConfiguration();
        this.enabled = this.configuration.isEnabled();

        this.start();
    }

    private void start() {
        if (this.enabled) {
            HyriCommonImplementation.log("Starting Hydrion client...");

            this.client = new HydrionClient(this.configuration.getUrl(), this.configuration.getAPIKey());
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public HydrionClient getClient() {
        return this.client;
    }

    public HydrionConfiguration getConfiguration() {
        return this.configuration;
    }

}
