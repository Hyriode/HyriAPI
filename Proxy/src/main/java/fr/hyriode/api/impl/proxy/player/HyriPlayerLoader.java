package fr.hyriode.api.impl.proxy.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.api.party.HyriPartyDisbandReason;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.nickname.IHyriNickname;

import java.util.Date;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 19:13
 */
public class HyriPlayerLoader {

    private final HydrionManager hydrionManager;

    public HyriPlayerLoader(HydrionManager hydrionManager) {
        this.hydrionManager = hydrionManager;
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

        if (this.hydrionManager.isEnabled()) {
            playerManager.sendPlayerToHydrion(account);

            this.hydrionManager.getClient().getUUIDModule().setUUID(name, uuid);
        }

        playerManager.setPlayerId(name, uuid);

        return account;
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


            HyriAPI.get().getFriendManager().removeFriends(uuid);

            pm.sendPlayerToHydrion(account);
            pm.removePlayer(uuid);
        }
    }

}