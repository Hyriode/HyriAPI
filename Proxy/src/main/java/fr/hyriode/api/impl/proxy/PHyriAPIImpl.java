package fr.hyriode.api.impl.proxy;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.IHyriEventBus;
import fr.hyriode.api.impl.common.CHyriAPIImpl;
import fr.hyriode.api.impl.proxy.listener.*;
import fr.hyriode.api.impl.proxy.player.PHyriPlayerManager;
import fr.hyriode.api.impl.proxy.receiver.ChatReceiver;
import fr.hyriode.api.impl.proxy.receiver.PlayerReceiver;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.pubsub.IHyriPubSub;
import fr.hyriode.api.server.join.IHyriJoinManager;
import fr.hyriode.hyggdrasil.api.protocol.HyggChannel;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggEnv;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketProcessor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;

import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 16:01
 */
public class PHyriAPIImpl extends CHyriAPIImpl {

    private HyriProxy proxy;

    private final HyriAPIPlugin plugin;

    public PHyriAPIImpl(HyriAPIPlugin plugin) {
        super(plugin.getConfiguration());
        this.plugin = plugin;

        this.preInit();
        this.init(null, plugin.getLogger());
        this.postInit();
    }

    @Override
    protected void init(HyggEnv environment, Logger logger) {
        super.init(environment, logger);

        this.proxy = new HyriProxy(this.hyggdrasilManager.withHyggdrasil() ? this.hyggdrasilManager.getApplication() : null);
        this.playerManager = new PHyriPlayerManager();
    }

    @Override
    protected void postInit() {
        super.postInit();

        // Load languages from files and resources
        this.languageManager.loadLanguagesMessages(Paths.get(this.plugin.getDataFolder().toPath().toString(), "lang"), "/lang/", HyriAPIPlugin.class::getResourceAsStream);

        // Register a language adapter
        this.languageManager.registerAdapter(ProxiedPlayer.class, (message, player) -> message.getValue(player.getUniqueId()));

        // Set network information
        final IHyriNetwork network = this.networkManager.getNetwork();

        if (network.getSlots() == -1) {
            network.setSlots(plugin.getConfiguration().getSlots());
        }

        if (network.getMotd() == null || !network.getMotd().equals(plugin.getConfiguration().getMotd())) {
            network.setMotd(plugin.getConfiguration().getMotd());
        }

        network.update();

        // Register receivers
        if (this.hyggdrasilManager.withHyggdrasil()) {
            new ServersListener().register();
            new LimbosListener().register();
        }

        final IHyriPubSub pubSub = HyriAPI.get().getPubSub();

        pubSub.subscribe(HyriChannel.PROXIES, new PlayerReceiver());
        pubSub.subscribe(HyriChannel.CHAT, new ChatReceiver());

        final Consumer<Listener> register = listener -> this.plugin.getProxy().getPluginManager().registerListener(this.plugin, listener);

        register.accept(new ProxyListener(this.plugin.getConfiguration()));
        register.accept(new JoinListener());

        final IHyriEventBus eventBus = HyriAPI.get().getNetworkManager().getEventBus();

        eventBus.register(new NetworkListener());
        eventBus.register(new RestartListener());
    }

    @Override
    public void log(Level level, String message) {
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

    @Override
    public HyriProxy getProxy() {
        return this.proxy;
    }

    @Override
    public IHyriJoinManager getJoinManager() {
        return null;
    }

}
