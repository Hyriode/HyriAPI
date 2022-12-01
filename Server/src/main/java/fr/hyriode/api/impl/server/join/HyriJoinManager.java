package fr.hyriode.api.impl.server.join;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.server.join.HyriJoinResponse;
import fr.hyriode.api.server.join.IHyriJoinHandler;
import fr.hyriode.api.server.join.IHyriJoinManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.TreeMap;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 21:54
 */
public class HyriJoinManager implements IHyriJoinManager {

    private final TreeMap<Integer, IHyriJoinHandler> handlers;

    public HyriJoinManager() {
        this.handlers = new TreeMap<>();

        HyriAPI.get().getPubSub().subscribe(HyriChannel.JOIN, new JoinReceiver(this));
    }

    @Override
    public void registerHandler(int priority, IHyriJoinHandler handler) {
        this.handlers.put(priority, handler);
    }

    void onLogin(AsyncPlayerPreLoginEvent event) {
        final UUID playerId = event.getUniqueId();

        if (!this.isExpected(playerId)) {
            final String message = this.requestPlayerJoin(playerId, false);

            if (message != null) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, message);
                return;
            }
        }

        for (IHyriJoinHandler handler : this.handlers.values()) {
            handler.onLogin(playerId, event.getName());
        }
    }

    void onJoin(Player player) {
        for (IHyriJoinHandler handler : this.handlers.values()) {
            handler.onJoin(player.getUniqueId());
        }
    }

    void onLogout(Player player) {
        for (IHyriJoinHandler handler : this.handlers.values()) {
            handler.onLogout(player.getUniqueId());
        }
    }

    String requestPlayerJoin(UUID playerId, boolean connect) {
        final HyriJoinResponse response = HyriJoinResponse.ALLOW;

        for (IHyriJoinHandler handler : this.handlers.values()) {
            handler.requestJoin(playerId, response);

            if (!response.isAllowed()) {
                return handler.createResponseMessage(playerId, response);
            }
        }

        if (connect) {
            HyriAPI.get().getPlayerManager().connectPlayer(playerId, HyriAPI.get().getServer().getName());
        }
        return null;
    }

    private boolean isExpected(UUID playerId) {
        for (IHyriJoinHandler handler : this.handlers.values()) {
            if (handler.isExpected(playerId)) {
                return true;
            }
        }
        return false;
    }

}
