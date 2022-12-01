package fr.hyriode.api.impl.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.CHyriAPIImpl;
import fr.hyriode.api.impl.server.join.HyriJoinManager;
import fr.hyriode.api.impl.server.join.JoinListener;
import fr.hyriode.api.impl.server.player.HyriPlayerManager;
import fr.hyriode.api.impl.server.receiver.SoundReceiver;
import fr.hyriode.api.impl.server.receiver.StopReceiver;
import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.server.join.IHyriJoinManager;
import fr.hyriode.api.sound.HyriSoundPacket;
import fr.hyriode.hyggdrasil.api.protocol.HyggChannel;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggEnv;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketProcessor;
import fr.hyriode.hystia.api.IHystiaAPI;
import fr.hyriode.hystia.spigot.HystiaSpigot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 11:12
 */
public class SHyriAPIImpl extends CHyriAPIImpl {

    private HyriServer server;
    private HyriJoinManager joinManager;

    private final HyriAPIPlugin plugin;

    public SHyriAPIImpl(HyriAPIPlugin plugin) {
        super(plugin.getConfiguration());
        this.plugin = plugin;

        this.preInit();
        this.init(null);
        this.postInit();
    }

    @Override
    protected void init(HyggEnv environment) {
        super.init(environment);

        this.server = new HyriServer(this.hyggdrasilManager.withHyggdrasil() ? this.hyggdrasilManager.getApplication() : null);
        this.playerManager = new HyriPlayerManager();
        this.joinManager = new HyriJoinManager();
        this.hystiaAPI = new HystiaSpigot(this.plugin, this.getMongoDB().getClient());
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

        // Register receivers
        if (this.hyggdrasilManager.withHyggdrasil()) {
            final HyggPacketProcessor processor = this.hyggdrasilManager.getHyggdrasilAPI().getPacketProcessor();

            processor.registerReceiver(HyggChannel.SERVERS, new StopReceiver());
        }

        this.pubSub.subscribe(HyriSoundPacket.CHANNEL, new SoundReceiver());

        // Register Spigot listeners
        final Consumer<Listener> register = listener -> this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);

        register.accept(new JoinListener());
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

        Bukkit.getConsoleSender().sendMessage(prefix + message);
    }

    @Override
    public HyriServer getServer() {
        return this.server;
    }

    @Override
    public IHystiaAPI getHystiaAPI() {
        return this.hystiaAPI;
    }

    @Override
    public IHyriJoinManager getJoinManager() {
        return this.joinManager;
    }

}
