package fr.hyriode.api.impl.common.network.counter;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.network.counter.IHyriCategoryCounter;
import fr.hyriode.hyggdrasil.api.server.HyggServer;

/**
 * Created by AstFaster
 * on 08/07/2022 at 10:40
 */
public class HyriCategoryCounter implements IHyriCategoryCounter {

    private final String name;

    public HyriCategoryCounter(String name) {
        this.name = name;
    }

    @Override
    public int getPlayers() {
        int players = 0;
        for (HyggServer server : HyriAPI.get().getServerManager().getServers(this.name)) {
            players += server.getPlayingPlayers().size();
        }
        return players;
    }

    @Override
    public int getPlayers(String type) {
        int players = 0;
        for (HyggServer server : HyriAPI.get().getServerManager().getServers(this.name)) {
            final String gameType = server.getGameType();

            if (gameType != null && gameType.equals(type)) {
                players += server.getPlayingPlayers().size();
            }
        }
        return players;
    }

}
