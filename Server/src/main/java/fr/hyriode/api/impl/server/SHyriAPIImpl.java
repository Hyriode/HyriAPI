package fr.hyriode.api.impl.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.impl.common.CHyriAPIImpl;
import fr.hyriode.api.impl.server.join.JoinManager;
import fr.hyriode.api.impl.server.join.JoinListener;
import fr.hyriode.api.impl.server.player.SHyriPlayerManager;
import fr.hyriode.api.impl.server.receiver.ServerReceiver;
import fr.hyriode.api.impl.server.world.SHyriWorldManager;
import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.server.join.IHyriJoinManager;
import fr.hyriode.hyggdrasil.api.protocol.HyggChannel;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggEnv;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketProcessor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 11:12
 */
public class SHyriAPIImpl extends CHyriAPIImpl {

    private HyriServer server;
    private JoinManager joinManager;

    private final HyriAPIPlugin plugin;

    public SHyriAPIImpl(HyriAPIPlugin plugin) {
        super(plugin.getConfiguration());
        this.plugin = plugin;

        this.preInit();
        this.init(null, plugin.getLogger());
        this.postInit();
    }

    @Override
    protected void init(HyggEnv environment, Logger logger) {
        super.init(environment, logger);

        this.server = new HyriServer(this.hyggdrasilManager.withHyggdrasil() ? this.hyggdrasilManager.getApplication() : null);
        this.worldManager = new SHyriWorldManager(this.mongoDB);
        this.playerManager = new SHyriPlayerManager();
        this.joinManager = new JoinManager();
    }

    @Override
    protected void postInit() {
        super.postInit();

        // Register a language adapter
        this.languageManager.registerAdapter(CommandSender.class, (message, sender) -> {
            if (sender instanceof Player) {
                return message.getValue(((Player) sender).getUniqueId());
            } else if (sender instanceof OfflinePlayer) {
                return message.getValue(((OfflinePlayer) sender).getUniqueId());
            }
            return message.getValue(HyriLanguage.EN);
        });

        this.pubSub.subscribe(HyriChannel.SERVERS, new ServerReceiver());

        // Register Spigot listeners
        final Consumer<Listener> register = listener -> this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);

        register.accept(new JoinListener());
    }

    private static class EmptyEvent extends HyriEvent {}

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

        Bukkit.getConsoleSender().sendMessage(prefix + message);
    }

    @Override
    public HyriServer getServer() {
        return this.server;
    }

    @Override
    public IHyriJoinManager getJoinManager() {
        return this.joinManager;
    }

}
