package fr.hyriode.api.impl.proxy.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriendManager;
import fr.hyriode.api.impl.proxy.HyriAPIPlugin;
import fr.hyriode.api.party.HyriPartyDisbandReason;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.event.PlayerQuitNetworkEvent;
import fr.hyriode.api.player.nickname.IHyriNickname;
import fr.hyriode.hylios.api.lobby.LobbyAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Date;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 19:13
 */
public class HyriPlayerLoader {

    private final HyriAPIPlugin plugin;

    public HyriPlayerLoader(HyriAPIPlugin plugin) {
        this.plugin = plugin;
    }

    public IHyriPlayer loadPlayerAccount(IHyriPlayer account, UUID uuid, String name) {
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();

        if (account == null) {
            account = playerManager.createPlayer(false, uuid, name);
        }

        account.setName(name);
        account.setLastLoginDate(new Date(System.currentTimeMillis()));
        account.setCurrentProxy(HyriAPI.get().getProxy().getName());
        account.setParty(null);
        account.update();

        playerManager.updatePlayer(account);
        playerManager.setPlayerId(name, uuid);

        return account;
    }

    public void handleDisconnection(ProxiedPlayer player) {
        final UUID playerId = player.getUniqueId();

        this.unloadPlayerAccount(playerId);

        HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new PlayerQuitNetworkEvent(playerId));

        this.plugin.getAPI().getProxy().removePlayer(playerId);
        this.plugin.getAPI().getHyggdrasilManager().sendData();
    }

    public void unloadPlayerAccount(UUID uuid) {
        final IHyriPlayer account = IHyriPlayer.get(uuid);
        final IHyriPlayerManager pm = HyriAPI.get().getPlayerManager();

        if (account != null) {
            final UUID partyId = account.getParty();

            if (partyId != null) {
                final IHyriParty party = HyriAPI.get().getPartyManager().getParty(partyId);

                if (party != null) {
                    final UUID playerId = account.getUniqueId();

                    if (party.isLeader(playerId)) {
                        party.disband(HyriPartyDisbandReason.LEADER_LEAVE);
                    } else {
                        party.removeMember(account.getUniqueId());
                    }
                }
            }

            HyriAPI.get().getQueueManager().removePlayerFromQueue(uuid);
            HyriAPI.get().getQueueManager().removePlayerQueue(uuid);

            account.setParty(null);
            account.setLastPrivateMessagePlayer(null);
            account.setPlayTime(account.getPlayTime() + (System.currentTimeMillis() - account.getLastLoginDate().getTime()));
            account.setCurrentServer(null);
            account.setLastServer(null);
            account.setInVanishMode(false);
            account.setInModerationMode(false);
            account.setOnline(false);
            account.setCurrentProxy(null);

            final IHyriNickname nickname = account.getNickname();

            if (nickname != null) {
                pm.getNicknameManager().removeUsedNickname(nickname.getName());

                account.setNickname(null);
            }

            final IHyriFriendManager friendManager = HyriAPI.get().getFriendManager();

            friendManager.updateFriends(friendManager.createHandler(uuid));
            friendManager.removeCachedFriends(uuid);

            pm.updatePlayer(account);
            pm.removeCachedPlayer(uuid);
        }
    }

}
