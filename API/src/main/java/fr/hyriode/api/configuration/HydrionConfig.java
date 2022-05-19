package fr.hyriode.api.configuration;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 11:56
 */
public class HydrionConfiguration {

    private final boolean enabled;
    private final String url;
    private final UUID apiKey;

    public HydrionConfiguration(boolean enabled, String url, UUID apiKey) {
        this.enabled = enabled;
        this.url = url;
        this.apiKey = apiKey;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getUrl() {
        return this.url;
    }

    public UUID getAPIKey() {
        return this.apiKey;
    }

}
