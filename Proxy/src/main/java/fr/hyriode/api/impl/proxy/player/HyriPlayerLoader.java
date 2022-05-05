package fr.hyriode.api.impl.proxy.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriendManager;
import fr.hyriode.api.impl.common.friend.HyriFriends;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.api.party.HyriPartyDisbandReason;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.nickname.IHyriNickname;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import fr.hyriode.hydrion.client.module.PlayerModule;
import fr.hyriode.hydrion.client.response.HydrionResponse;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Function;

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

    public IHyriPlayer loadPlayerAccount(UUID uuid, String name) {
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();

        IHyriPlayer account = null;
        try {
            account = playerManager.getPlayerFromHydrion(uuid).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

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

    public boolean unloadPlayerAccount(UUID uuid) {
        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(uuid);

        if (account != null) {
            boolean result = false;

            if (account.isOnline()) {
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

                final IHyriNickname nickname = account.getNickname();

                if (nickname != null) {
                    HyriAPI.get().getPlayerManager().getNicknameManager().removeUsedNickname(nickname.getName());

                    account.setNickname(null);
                }

                result = true;
            }

            account.setOnline(false);
            account.setCurrentProxy(null);
            account.update();

            return result;
        }
        return false;
    }

}
