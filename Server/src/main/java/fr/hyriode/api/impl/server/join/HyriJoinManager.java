package fr.hyriode.api.impl.server.join;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.server.join.HyriJoinResponse;
import fr.hyriode.api.server.join.IHyriJoinHandler;
import fr.hyriode.api.server.join.IHyriJoinManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 21:54
 */
public class HyriJoinManager implements IHyriJoinManager {

    private final List<UUID> expectedPlayers;
    private final List<UUID> expectedModerators;

    private final TreeMap<Integer, IHyriJoinHandler> handlers;

    private final JavaPlugin plugin;

    public HyriJoinManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.handlers = new TreeMap<>();
        this.expectedPlayers = new ArrayList<>();
        this.expectedModerators = new ArrayList<>();

        HyriAPI.get().getPubSub().subscribe(HyriChannel.JOIN, new HyriJoinReceiver(this));
    }

    void onLogin(AsyncPlayerPreLoginEvent event) {
        final UUID playerId = event.getUniqueId();

        if (this.expectedModerators.contains(playerId)) {
            return;
        }

        if (!this.expectedPlayers.contains(playerId)) {
            final String message = this.requestPlayerJoin(playerId, false);

            if (message != null) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, message);
                return;
            }
        }

        this.expectedPlayers.remove(playerId);

        for (IHyriJoinHandler handler : this.handlers.values()) {
            handler.onLogin(playerId, event.getName());
        }
    }

    void onJoin(Player player) {
        final UUID playerId = player.getUniqueId();

        if (this.expectedModerators.remove(playerId)) {
            for (IHyriJoinHandler handler : this.handlers.values()) {
                handler.onModeratorJoin(playerId);
                return;
            }
        }

        for (IHyriJoinHandler handler : this.handlers.values()) {
            handler.onPlayerJoin(playerId);
        }
    }

    void onLogout(Player player) {
        for (IHyriJoinHandler handler : this.handlers.values()) {
            handler.onLogout(player.getUniqueId());
        }
    }

    String requestPlayerJoin(UUID playerId, boolean connect) {
        HyriJoinResponse response = HyriJoinResponse.ALLOW;

        for (IHyriJoinHandler handler : this.handlers.values()) {
            response = handler.requestJoin(playerId, response);

            if (!response.isAllowed()) {
                final String message = handler.createResponseMessage(playerId, response);

                HyriAPI.get().getPlayerManager().sendMessage(playerId, message);

                return message;
            }
        }

        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(playerId);

        if (account.isInModerationMode()) {
            this.addExpectedModerator(playerId);
        } else {
            this.addExpectedPlayer(playerId);
        }

        if (connect) {
            System.out.println("Connecting " + playerId);
            HyriAPI.get().getPlayerManager().connectPlayer(playerId, HyriAPI.get().getServer().getName());
        }
        return null;
    }

    void requestPartyJoin(UUID partyId) {
        final IHyriParty party = HyriAPI.get().getPartyManager().getParty(partyId);

        if (party == null) {
            return;
        }

        HyriJoinResponse response = HyriJoinResponse.ALLOW;

        for (IHyriJoinHandler handler : this.handlers.values()) {
            response = handler.requestPartyJoin(partyId, response);

            if (!response.isAllowed()) {
                return;
            }
        }

        final String serverName = HyriAPI.get().getServer().getName();

        for (UUID playerId : party.getMembers().keySet()) {
            this.addExpectedPlayer(playerId);

            HyriAPI.get().getPlayerManager().connectPlayer(playerId, serverName);
        }
    }

    @Override
    public void addExpectedPlayer(UUID playerId) {
        this.expectedModerators.remove(playerId);
        this.expectedPlayers.add(playerId);

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.removeExpectedPlayer(playerId), 20 * 5L);
    }

    @Override
    public void removeExpectedPlayer(UUID playerId) {
        this.expectedPlayers.remove(playerId);
    }

    @Override
    public void registerHandler(int priority, IHyriJoinHandler handler) {
        this.handlers.put(priority, handler);
    }

    @Override
    public List<UUID> getExpectedPlayers() {
        return this.expectedPlayers;
    }

    @Override
    public List<UUID> getExpectedModerators() {
        return this.expectedModerators;
    }

    @Override
    public void addExpectedModerator(UUID playerId) {
        this.expectedModerators.remove(playerId);
        this.expectedModerators.add(playerId);

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.removeExpectedModerator(playerId), 20 * 5L);
    }

    @Override
    public void removeExpectedModerator(UUID playerId) {
        this.expectedModerators.remove(playerId);
    }

}
