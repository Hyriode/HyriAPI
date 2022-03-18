package fr.hyriode.api.impl.server;

import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.impl.server.money.HyriMoneyManager;
import fr.hyriode.api.impl.server.player.HyriPlayerManager;
import fr.hyriode.api.impl.server.rank.HyriRankManager;
import fr.hyriode.api.impl.server.receiver.HyriServerReceiver;
import fr.hyriode.api.money.IHyriMoneyManager;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.rank.IHyriRankManager;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.hyggdrasil.api.protocol.HyggChannel;
import fr.hyriode.hyggdrasil.api.protocol.environment.HyggApplication;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketProcessor;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 11:12
 */
public class HyriAPIImplementation extends HyriCommonImplementation {

    private final IHyriServer server;

    private final IHyriPlayerManager playerManager;

    private final IHyriMoneyManager moneyManager;

    private final IHyriRankManager rankManager;

    private final HyriAPIPlugin plugin;

    public HyriAPIImplementation(HyriAPIPlugin plugin) {
        super(plugin.getConfiguration(), plugin.getLogger(), HyriAPIPlugin::log);
        this.plugin = plugin;
        this.server = this.createServer();
        this.playerManager = new HyriPlayerManager(this);
        this.moneyManager = new HyriMoneyManager();
        this.rankManager = new HyriRankManager();

        this.hyggdrasilManager.start();
        this.registerReceivers();
    }

    private IHyriServer createServer() {
        if (this.hyggdrasilManager.withHyggdrasil()) {
            final HyggApplication application = this.hyggdrasilManager.getApplication();

            return new HyriServer(this.hyggdrasilManager, application.getName(), application.getStartedTime());
        }
        return new HyriServer(this.hyggdrasilManager, this.hyggdrasilManager.generateDevApplicationName(), System.currentTimeMillis());
    }

    private void registerReceivers() {
        if (this.hyggdrasilManager.withHyggdrasil()) {
            final HyggPacketProcessor processor = this.hyggdrasilManager.getHyggdrasilAPI().getPacketProcessor();

            processor.registerReceiver(HyggChannel.SERVERS, new HyriServerReceiver());
        }
    }

    @Override
    public IHyriServer getServer() {
        return this.server;
    }

    @Override
    public IHyriPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    public IHyriMoneyManager getMoneyManager() {
        return this.moneyManager;
    }

    @Override
    public IHyriRankManager getRankManager() {
        return this.rankManager;
    }

}
