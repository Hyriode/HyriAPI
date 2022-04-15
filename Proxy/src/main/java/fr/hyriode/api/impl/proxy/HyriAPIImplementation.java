package fr.hyriode.api.impl.proxy;

import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.proxy.player.HyriPlayerManager;
import fr.hyriode.api.impl.proxy.receiver.HyriProxyReceiver;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.hyggdrasil.api.protocol.HyggChannel;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketProcessor;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 16:01
 */
public class HyriAPIImplementation extends HyriCommonImplementation {

    private final IHyriProxy proxy;

    private final IHyriPlayerManager playerManager;

    public HyriAPIImplementation(HyriAPIPlugin plugin) {
        super(plugin.getConfiguration(), plugin.getLogger(), HyriAPIPlugin::log);
        this.proxy = this.createProxy();
        this.playerManager = new HyriPlayerManager(this.hydrionManager);

        final IHyriNetwork network = this.networkManager.getNetwork();

        if (network.getSlots() == -1) {
            network.setSlots(plugin.getConfiguration().getSlots());
            network.update();
        }

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
        if (this.hyggdrasilManager.withHyggdrasil()) {
            final HyggPacketProcessor processor = this.hyggdrasilManager.getHyggdrasilAPI().getPacketProcessor();

            processor.registerReceiver(HyggChannel.PROXIES, new HyriProxyReceiver());
        }
    }

    @Override
    public IHyriProxy getProxy() {
        return this.proxy;
    }

    @Override
    public IHyriPlayerManager getPlayerManager() {
        return this.playerManager;
    }

}
