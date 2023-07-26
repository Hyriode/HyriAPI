package fr.hyriode.api.impl.proxy;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.proxy.config.HyriAPIConfig;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxy;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.net.InetSocketAddress;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 15:58
 */
public class HyriAPIPlugin extends Plugin  {

    private HyriAPIConfig configuration;
    private PHyriAPIImpl api;

    @Override
    public void onEnable() {
        this.configuration = HyriAPIConfig.Loader.load(this);
        this.api = new PHyriAPIImpl(this);

        HyriAPI.get().getProxy().setState(HyggProxy.State.READY);

        new TemporarilySyncTask();
    }

    @Override
    public void onDisable() {
        this.api.stop();

        HyriAPI.get().log(HyriAPI.NAME + " is now disabled. See you soon!");
    }

    public HyriAPIConfig getConfiguration() {
        return this.configuration;
    }

    public PHyriAPIImpl getAPI() {
        return this.api;
    }

}
