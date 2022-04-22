package fr.hyriode.api.impl.proxy;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.proxy.player.HyriPlayerManager;
import fr.hyriode.api.impl.proxy.receiver.HyriChatReceiver;
import fr.hyriode.api.impl.proxy.receiver.HyriPlayerReceiver;
import fr.hyriode.api.impl.proxy.receiver.HyriProxyReceiver;
import fr.hyriode.api.impl.proxy.receiver.HyriQueueReceiver;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.api.pubsub.IHyriPubSub;
import fr.hyriode.hyggdrasil.api.protocol.HyggChannel;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketProcessor;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 16:01
 */
public class HyriAPIImplementation extends HyriCommonImplementation {

    private final IHyriProxy proxy;

    private final HyriPlayerManager playerManager;

    public HyriAPIImplementation(HyriAPIPlugin plugin) {
        super(plugin.getConfiguration(), plugin.getLogger(), HyriAPIPlugin::log);
        this.proxy = this.createProxy();
        this.playerManager = new HyriPlayerManager(this.hydrionManager);

        final IHyriNetwork network = this.networkManager.getNetwork();

        if (network.getSlots() == -1) {
            network.setSlots(plugin.getConfiguration().getSlots());
            network.update();
        }

        if (network.getMotd() == null) {
            network.setMotd(plugin.getConfiguration().getMotd());
            network.update();
        }

        network.getMaintenance().enable(UUID.randomUUID(), null);
        network.update();

        this.hyggdrasilManager.start();

        this.registerReceivers();
    }

    private IHyriProxy createProxy() {
        if (this.hyggdrasilManager.withHyggdrasil()) {
            final HyggApplication application = this.hyggdrasilManager.getApplication();

            return new HyriProxy(this.hyggdrasilManager, application.getName(), application.getStartedTime());
        }
        return new HyriProxy(this.hyggdrasilManager, this.hyggdrasilManager.generateDevApplicationName(), System.currentTimeMillis());
    }

    private void registerReceivers() {
        final HyriProxyReceiver proxyReceiver = new HyriProxyReceiver();

        if (this.hyggdrasilManager.withHyggdrasil()) {
            final HyggPacketProcessor processor = this.hyggdrasilManager.getHyggdrasilAPI().getPacketProcessor();

            processor.registerReceiver(HyggChannel.PROXIES, proxyReceiver);
            processor.registerReceiver(HyggChannel.QUEUE, new HyriQueueReceiver());
        }

        final IHyriPubSub pubSub = HyriAPI.get().getPubSub();

        pubSub.subscribe(HyriChannel.PROXIES, proxyReceiver);
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

}
