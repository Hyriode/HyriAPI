package fr.hyriode.api.impl.server;

import fr.hyriode.api.chat.channel.IHyriChatChannelHandler;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.server.chat.GlobalChatHandler;
import fr.hyriode.api.impl.server.chat.PartnerChatHandler;
import fr.hyriode.api.impl.server.chat.PartyChatHandler;
import fr.hyriode.api.impl.server.chat.StaffChatHandler;
import fr.hyriode.api.impl.server.player.HyriPlayerManager;
import fr.hyriode.api.impl.server.receiver.HyriChatReceiver;
import fr.hyriode.api.impl.server.receiver.HyriServerReceiver;
import fr.hyriode.api.impl.server.receiver.HyriSoundReceiver;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.sound.HyriSoundPacket;
import fr.hyriode.hyggdrasil.api.protocol.HyggChannel;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggData;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketProcessor;
import fr.hyriode.hystia.api.IHystiaAPI;
import fr.hyriode.hystia.spigot.HystiaSpigot;

import java.util.Arrays;
import java.util.List;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 11:12
 */
public class HyriAPIImplementation extends HyriCommonImplementation {

    private final HyriServer server;

    private final HyriServerManager serverManager;

    public HyriAPIImplementation(HyriAPIPlugin plugin) {
        super(plugin.getConfiguration(), plugin.getLogger(), HyriAPIPlugin::log);
        this.server = this.createServer();
        this.serverManager = new HyriServerManager(plugin, this);
        this.playerManager = new HyriPlayerManager();
        this.hystiaAPI = new HystiaSpigot(plugin, this.getMongoDB().getClient());
        this.queueManager.start();

        this.registerReceivers();
        this.registerChatHandlers();
    }

    private HyriServer createServer() {
        if (this.hyggdrasilManager.withHyggdrasil()) {
            final HyggApplication application = this.hyggdrasilManager.getApplication();

            return new HyriServer(this.hyggdrasilManager, application.getName(), application.getStartedTime(), this.hyggdrasilManager.getEnvironment().getData());
        }
        return new HyriServer(this.hyggdrasilManager, this.hyggdrasilManager.generateDevApplicationName(), System.currentTimeMillis(), new HyggData());
    }

    private void registerReceivers() {
        if (this.hyggdrasilManager.withHyggdrasil()) {
            final HyggPacketProcessor processor = this.hyggdrasilManager.getHyggdrasilAPI().getPacketProcessor();

            processor.registerReceiver(HyggChannel.SERVERS, new HyriServerReceiver());
        }

        this.pubSub.subscribe(HyriChannel.CHAT, new HyriChatReceiver());
        this.pubSub.subscribe(HyriSoundPacket.CHANNEL, new HyriSoundReceiver());
    }

    private void registerChatHandlers() {
        final List<IHyriChatChannelHandler> handlers = Arrays.asList(new PartnerChatHandler(), new StaffChatHandler(), new GlobalChatHandler(), new PartyChatHandler());

        handlers.forEach(this.chatChannelManager::registerChannel);
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
    public HyriServerManager getServerManager() {
        return this.serverManager;
    }

}
