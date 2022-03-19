package fr.hyriode.api.impl.proxy;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.configuration.IHyriAPIConfiguration;
import fr.hyriode.api.impl.proxy.configuration.HyriAPIConfiguration;
import fr.hyriode.api.impl.proxy.listener.HyriJoinListener;
import fr.hyriode.api.impl.proxy.listener.HyriProxyListener;
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
public class HyriAPIPlugin extends Plugin {

    private IHyriAPIConfiguration configuration;

    private HyriAPIImplementation api;

    @Override
    public void onEnable() {
        this.configuration = HyriAPIConfiguration.Loader.load(this);
        this.api = new HyriAPIImplementation(this);

        this.registerListeners();
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

        register.accept(new HyriProxyListener());
        register.accept(new HyriJoinListener(this.api.getHyggdrasilManager()));
    }

    public IHyriAPIConfiguration getConfiguration() {
        return this.configuration;
    }

    public HyriAPIImplementation getAPI() {
        return this.api;
    }

}
