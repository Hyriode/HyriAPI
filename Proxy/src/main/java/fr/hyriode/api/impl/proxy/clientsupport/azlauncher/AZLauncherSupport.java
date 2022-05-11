package fr.hyriode.api.impl.proxy.clientsupport.azlauncher;

import fr.hyriode.api.impl.proxy.clientsupport.ClientSupport;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.event.EventHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 11/05/2022 at 14:14
 */
public class AZLauncherSupport extends ClientSupport implements Listener {

    private static final Pattern PACTIFY_HOSTNAME_PATTERN = Pattern.compile("\u0000(PAC[0-9A-F]{5})\u0000");

    public AZLauncherSupport(Plugin plugin) {
        super("azlauncher");

        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onPlayerHandshake(PlayerHandshakeEvent event) {
        final InitialHandler handler = (InitialHandler) event.getConnection();
        final Matcher matcher = PACTIFY_HOSTNAME_PATTERN.matcher(handler.getExtraDataInHandshake());

        if (matcher.find()) {
            // Send the Pactify Launcher handshake using \u0002 instead of \u0000 (so that will not break IP forwarding)
            event.getHandshake().setHost(event.getHandshake().getHost() + "\u0002" + matcher.group(1) + "\u0002");
        }
    }

}
