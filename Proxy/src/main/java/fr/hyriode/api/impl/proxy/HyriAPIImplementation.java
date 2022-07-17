package fr.hyriode.api.impl.proxy;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.proxy.player.HyriPlayerManager;
import fr.hyriode.api.impl.proxy.receiver.HyriChatReceiver;
import fr.hyriode.api.impl.proxy.receiver.HyriPlayerReceiver;
import fr.hyriode.api.impl.proxy.receiver.HyriProxyReceiver;
import fr.hyriode.api.impl.proxy.server.HyriServerManager;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.api.pubsub.IHyriPubSub;
import fr.hyriode.hyggdrasil.api.protocol.HyggChannel;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggData;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketProcessor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 16:01
 */
public class HyriAPIImplementation extends HyriCommonImplementation {

    private final IHyriProxy proxy;

    private final HyriServerManager serverManager;

    public HyriAPIImplementation(HyriAPIPlugin plugin) {
        super(plugin.getConfiguration(), plugin.getLogger(), HyriAPIPlugin::log);
        this.proxy = this.createProxy();
        this.playerManager = new HyriPlayerManager();
        this.serverManager = new HyriServerManager(this);

        this.languageManager.registerAdapter(ProxiedPlayer.class, (message, player) -> message.getValue(player.getUniqueId()));

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
        final HyriProxyReceiver proxyReceiver = new HyriProxyReceiver();

        if (this.hyggdrasilManager.withHyggdrasil()) {
            final HyggPacketProcessor processor = this.hyggdrasilManager.getHyggdrasilAPI().getPacketProcessor();

            processor.registerReceiver(HyggChannel.PROXIES, proxyReceiver);
        }

        final IHyriPubSub pubSub = HyriAPI.get().getPubSub();

        pubSub.subscribe(HyriChannel.PROXIES, new HyriPlayerReceiver());
        pubSub.subscribe(HyriChannel.PROXIES, proxyReceiver);
        pubSub.subscribe(HyriChannel.CHAT, new HyriChatReceiver());
    }

    @Override
    public IHyriProxy getProxy() {
        return this.proxy;
    }

    @Override
    public HyriServerManager getServerManager() {
        return this.serverManager;
    }

}
