package fr.hyriode.api.impl.common.configuration;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/11/2021 at 15:13
 */
public class HyriRedisConfiguration {

    private final String hostname;
    private final int port;
    private final String password;

    public HyriRedisConfiguration(String hostname, int port, String password) {
        this.hostname = hostname;
        this.port = port;
        this.password = password;
    }

    public String getHostname() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public String getPassword() {
        return this.password;
    }

}
