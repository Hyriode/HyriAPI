package fr.hyriode.api.config;

/**
 * Created by AstFaster
 * on 07/07/2022 at 20:03
 */
public class MongoDBConfig {

    private final String username;
    private final String password;
    private final String hostname;
    private final int port;

    public MongoDBConfig() {
        this("", "", "", 27017);
    }

    public MongoDBConfig(String username, String password, String hostname, int port) {
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

    public String toURL() {
        String url = "mongodb://";

        if (this.username != null && !this.username.equals("")) {
            url += this.username;
        }

        if (this.password != null && !this.password.equals("")) {
            url += ":" + this.password;
        }

        if ((this.username != null && !this.username.equals("")) || (this.password != null && !this.password.equals(""))) {
            url += "@";
        }

        return url + (this.hostname + ":" + this.port);
    }

}
