package fr.hyriode.api.impl.server;

import fr.hyriode.api.chat.IHyriChatChannelHandler;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.server.chat.*;
import fr.hyriode.api.impl.server.player.HyriPlayerManager;
import fr.hyriode.api.impl.server.receiver.HyriChatReceiver;
import fr.hyriode.api.impl.server.receiver.HyriServerReceiver;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.hyggdrasil.api.protocol.HyggChannel;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggData;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketProcessor;

import java.util.Arrays;
import java.util.List;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 11:12
 */
public class HyriAPIImplementation extends HyriCommonImplementation {

    private final IHyriServer server;

    private final IHyriPlayerManager playerManager;

    public HyriAPIImplementation(HyriAPIPlugin plugin) {
        super(plugin.getConfiguration(), plugin.getLogger(), HyriAPIPlugin::log);
        this.server = this.createServer();
        this.playerManager = new HyriPlayerManager(this.hydrionManager);

        this.hyggdrasilManager.start();
        this.registerReceivers();

        this.registerChatHandlers();
    }

    private IHyriServer createServer() {
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

        this.getPubSub().subscribe(HyriChannel.CHAT, new HyriChatReceiver());
    }

    private void registerChatHandlers() {
        final List<IHyriChatChannelHandler> handlers = Arrays.asList(new PartnerChatHandler(), new StaffChatHandler(), new GlobalChatHandler(), new PrivateChatHandler(), new PluginChatHandler());

        handlers.forEach(handler -> this.getChatChannelManager().registerChannel(handler));
    }

    @Override
    public IHyriServer getServer() {
        return this.server;
    }

    @Override
    public IHyriPlayerManager getPlayerManager() {
        return this.playerManager;
    }

}
