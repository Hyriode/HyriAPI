package fr.hyriode.api.impl.server;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.IHyriChatChannelHandler;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.common.network.HyriNetwork;
import fr.hyriode.api.impl.server.chat.GlobalChatHandler;
import fr.hyriode.api.impl.server.chat.PartnerChatHandler;
import fr.hyriode.api.impl.server.chat.StaffChatHandler;
import fr.hyriode.api.impl.server.player.HyriPlayerManager;
import fr.hyriode.api.impl.server.receiver.HyriChatReceiver;
import fr.hyriode.api.impl.server.receiver.HyriServerReceiver;
import fr.hyriode.api.impl.server.receiver.HyriSoundReceiver;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.api.sound.HyriSoundPacket;
import fr.hyriode.hydrion.client.HydrionClient;
import fr.hyriode.hydrion.client.response.HydrionResponse;
import fr.hyriode.hyggdrasil.api.protocol.HyggChannel;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggData;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketProcessor;
import fr.hyriode.hystia.api.IHystiaAPI;
import fr.hyriode.hystia.spigot.HystiaImpl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 11:12
 */
public class HyriAPIImplementation extends HyriCommonImplementation {

    private final HyriServer server;

    private IHystiaAPI hystiaAPI;

    private final HyriServerManager serverManager;

    private final IHyriPlayerManager playerManager;

    public HyriAPIImplementation(HyriAPIPlugin plugin) {
        super(plugin.getConfiguration(), plugin.getLogger(), HyriAPIPlugin::log);
        this.server = this.createServer();
        this.serverManager = new HyriServerManager(plugin, this);
        this.playerManager = new HyriPlayerManager(this.hydrionManager);

        if (this.hydrionManager.isEnabled()) {
            this.hystiaAPI = new HystiaImpl(plugin, this.hydrionManager.getClient());
        }

        if (HyriAPI.get().getConfiguration().isDevEnvironment()) {
            this.networkManager.cacheNetwork(new HyriNetwork());
        }

        this.hyggdrasilManager.start();
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
        final List<IHyriChatChannelHandler> handlers = Arrays.asList(new PartnerChatHandler(), new StaffChatHandler(), new GlobalChatHandler());

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
    public IHyriPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    public HyriServerManager getServerManager() {
        return this.serverManager;
    }

}
