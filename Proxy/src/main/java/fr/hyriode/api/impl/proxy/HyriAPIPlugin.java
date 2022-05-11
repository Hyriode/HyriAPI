package fr.hyriode.api.impl.proxy;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.proxy.clientsupport.ClientSupportManager;
import fr.hyriode.api.impl.proxy.clientsupport.azlauncher.AZLauncherSupport;
import fr.hyriode.api.impl.proxy.configuration.HyriAPIConfiguration;
import fr.hyriode.api.impl.proxy.listener.HyriJoinListener;
import fr.hyriode.api.impl.proxy.listener.HyriProxyListener;
import fr.hyriode.api.impl.proxy.player.HyriPlayerLoader;
import fr.hyriode.api.impl.proxy.player.HyriOnlinePlayersTask;
import fr.hyriode.api.proxy.IHyriProxy;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 15:58
 */
public class HyriAPIPlugin extends Plugin  {

    private HyriAPIConfiguration configuration;
    private HyriAPIImplementation api;

    private HyriPlayerLoader playerLoader;
    private HyriOnlinePlayersTask onlinePlayersTask;

    private ClientSupportManager clientSupportManager;

    @Override
    public void onEnable() {
        this.configuration = HyriAPIConfiguration.Loader.load(this);
        this.api = new HyriAPIImplementation(this);

        this.playerLoader = new HyriPlayerLoader(this.api.getHydrionManager());
        this.onlinePlayersTask = new HyriOnlinePlayersTask();
        this.onlinePlayersTask.start(this);

        this.clientSupportManager = new ClientSupportManager();
        this.clientSupportManager.registerSupport(new AZLauncherSupport(this));

        this.registerListeners();

        HyriAPI.get().getProxy().setState(IHyriProxy.State.READY);
    }

    @Override
    public void onDisable() {
        this.api.stop();

        log(HyriAPI.NAME + " is now disabled. See you soon!");
    }

    public static void log(Level level, String message) {
        String prefix = ChatColor.DARK_AQUA + "[" + HyriAPI.NAME + "] ";

        if (level == Level.SEVERE) {
            prefix += ChatColor.RED;
        } else if (level == Level.WARNING) {
            prefix += ChatColor.YELLOW;
        } else {
            prefix += ChatColor.RESET;
        }

        ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText(prefix + message));
    }

    public static void log(String message) {
        log(Level.INFO, message);
    }

    private void registerListeners() {
        final Consumer<Listener> register = listener -> this.getProxy().getPluginManager().registerListener(this, listener);

        register.accept(new HyriProxyListener(this.configuration));
        register.accept(new HyriJoinListener(this));
    }

    public HyriAPIConfiguration getConfiguration() {
        return this.configuration;
    }

    public HyriAPIImplementation getAPI() {
        return this.api;
    }

    public HyriPlayerLoader getPlayerLoader() {
        return this.playerLoader;
    }

    public HyriOnlinePlayersTask getOnlinePlayersTask() {
        return this.onlinePlayersTask;
    }

    public ClientSupportManager getClientSupportManager() {
        return this.clientSupportManager;
    }

}
