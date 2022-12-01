package fr.hyriode.api.impl.proxy;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.proxy.clientsupport.ClientSupportManager;
import fr.hyriode.api.impl.proxy.clientsupport.azlauncher.AZLauncherSupport;
import fr.hyriode.api.impl.proxy.config.HyriAPIConfig;
import fr.hyriode.api.impl.proxy.listener.JoinListener;
import fr.hyriode.api.impl.proxy.listener.NetworkListener;
import fr.hyriode.api.impl.proxy.listener.ProxyListener;
import fr.hyriode.api.impl.proxy.player.PlayerLoader;
import fr.hyriode.api.impl.proxy.task.OnlinePlayersTask;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxy;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 15:58
 */
public class HyriAPIPlugin extends Plugin  {

    private HyriAPIConfig configuration;
    private PHyriAPIImpl api;

    private PlayerLoader playerLoader;

    private OnlinePlayersTask onlinePlayersTask;

    private ClientSupportManager clientSupportManager;

    @Override
    public void onEnable() {
        this.configuration = HyriAPIConfig.Loader.load(this);
        this.api = new PHyriAPIImpl(this);
        this.playerLoader = new PlayerLoader();
        this.onlinePlayersTask = new OnlinePlayersTask();
        this.onlinePlayersTask.start(this);
        this.clientSupportManager = new ClientSupportManager();
        this.clientSupportManager.registerSupport(new AZLauncherSupport(this));

        this.registerListeners();

        HyriAPI.get().getProxy().setState(HyggProxy.State.READY);
    }

    @Override
    public void onDisable() {
        this.api.stop();
        this.onlinePlayersTask.stop();

        HyriAPI.get().log(HyriAPI.NAME + " is now disabled. See you soon!");
    }

    private void registerListeners() {
        final Consumer<Listener> register = listener -> this.getProxy().getPluginManager().registerListener(this, listener);

        register.accept(new ProxyListener(this.configuration));
        register.accept(new JoinListener(this));

        HyriAPI.get().getNetworkManager().getEventBus().register(new NetworkListener());
    }

    public HyriAPIConfig getConfiguration() {
        return this.configuration;
    }

    public PHyriAPIImpl getAPI() {
        return this.api;
    }

    public PlayerLoader getPlayerLoader() {
        return this.playerLoader;
    }

    public OnlinePlayersTask getOnlinePlayersTask() {
        return this.onlinePlayersTask;
    }

    public ClientSupportManager getClientSupportManager() {
        return this.clientSupportManager;
    }

}
