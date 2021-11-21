package fr.hyriode.hyriapi.impl.configuration.nested;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/11/2021 at 15:13
 */
public class RedisConfiguration {

    private String ip;
    private int port;
    private String password;

    public RedisConfiguration(String ip, int port, String password) {
        this.ip = ip;
        this.port = port;
        this.password = password;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void setDefault(ConfigurationSection section) {
        section.set("ip", "127.0.0.1");
        section.set("port", 6379);
        section.set("password", "");
    }

    public static RedisConfiguration build(ConfigurationSection section) {
        return new RedisConfiguration(section.getString("ip"), section.getInt("port"), section.getString("password"));
    }


}
