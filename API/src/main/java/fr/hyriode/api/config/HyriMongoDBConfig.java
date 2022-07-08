package fr.hyriode.api.config;

/**
 * Created by AstFaster
 * on 07/07/2022 at 20:03
 */
public class HyriMongoDBConfig {

    private final String username;
    private final String password;
    private final String hostname;
    private final int port;

    public HyriMongoDBConfig(String username, String password, String hostname, int port) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getHostname() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

}
