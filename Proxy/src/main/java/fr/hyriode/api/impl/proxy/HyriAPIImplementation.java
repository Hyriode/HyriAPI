package fr.hyriode.api.impl.proxy;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.proxy.player.HyriPlayerManager;
import fr.hyriode.api.impl.proxy.receiver.HyriChatReceiver;
import fr.hyriode.api.impl.proxy.receiver.HyriPlayerReceiver;
import fr.hyriode.api.impl.proxy.receiver.HyriProxyReceiver;
import fr.hyriode.api.impl.proxy.receiver.HyriQueueReceiver;
import fr.hyriode.api.impl.proxy.server.HyriServerManager;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.api.pubsub.IHyriPubSub;
import fr.hyriode.hyggdrasil.api.protocol.HyggChannel;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggData;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketProcessor;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 16:01
 */
public class HyriAPIImplementation extends HyriCommonImplementation {

    private final IHyriProxy proxy;

    private final HyriServerManager serverManager;

    private final HyriPlayerManager playerManager;

    public HyriAPIImplementation(HyriAPIPlugin plugin) {
        super(plugin.getConfiguration(), plugin.getLogger(), HyriAPIPlugin::log);
        this.proxy = this.createProxy();
        this.playerManager = new HyriPlayerManager(this.hydrionManager);
        this.serverManager = new HyriServerManager(this);

        this.hyggdrasilManager.start();

        final IHyriNetwork network = this.networkManager.getNetwork();

        if (network.getSlots() == -1) {
            network.setSlots(plugin.getConfiguration().getSlots());
        }

        if (network.getMotd() == null) {
            network.setMotd(plugin.getConfiguration().getMotd());
        }

        if (HyriAPI.get().getProxy().getData().get("first-proxy") != null) {
            network.setSlots(plugin.getConfiguration().getSlots());
            network.setMotd(plugin.getConfiguration().getMotd());
            network.getMaintenance().enable(UUID.randomUUID(), null);
            network.getPlayerCount().setPlayers(0);

            for (IHyriGameInfo gameInfo : this.gameManager.getGamesInfo()) {
                if (gameInfo != null) {
                    this.gameManager.deleteGameInfoFromRedis(gameInfo.getName());
                }
            }

            this.gameManager.getGamesInfo();
        }

        this.networkManager.setNetwork(network);

        this.registerReceivers();
    }

    private IHyriProxy createProxy() {
        if (this.hyggdrasilManager.withHyggdrasil()) {
            final HyggApplication application = this.hyggdrasilManager.getApplication();

            return new HyriProxy(this.hyggdrasilManager, application.getName(), application.getStartedTime(), this.hyggdrasilManager.getEnvironment().getData());
        }
        return new HyriProxy(this.hyggdrasilManager, this.hyggdrasilManager.generateDevApplicationName(), System.currentTimeMillis(), new HyggData());
    }

    private void registerReceivers() {
        if (this.hyggdrasilManager.withHyggdrasil()) {
            final HyggPacketProcessor processor = this.hyggdrasilManager.getHyggdrasilAPI().getPacketProcessor();

            processor.registerReceiver(HyggChannel.PROXIES, new HyriProxyReceiver());
            processor.registerReceiver(HyggChannel.QUEUE, new HyriQueueReceiver());
        }

        final IHyriPubSub pubSub = HyriAPI.get().getPubSub();

        pubSub.subscribe(HyriChannel.PROXIES, new HyriPlayerReceiver());
        pubSub.subscribe(HyriChannel.CHAT, new HyriChatReceiver());
    }

    @Override
    public IHyriProxy getProxy() {
        return this.proxy;
    }

    @Override
    public HyriPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    public HyriServerManager getServerManager() {
        return this.serverManager;
    }

}
