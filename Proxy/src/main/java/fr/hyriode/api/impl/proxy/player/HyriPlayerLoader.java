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
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 19:13
 */
public class HyriPlayerLoader {

    private final HydrionManager hydrionManager;
    private PlayerModule playerModule;

    public HyriPlayerLoader(HydrionManager hydrionManager) {
        this.hydrionManager = hydrionManager;

        if (this.hydrionManager.isEnabled()) {
            this.playerModule = this.hydrionManager.getClient().getPlayerModule();
        }
    }

    public IHyriPlayer loadPlayerAccount(UUID uuid, String name) {
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();

        IHyriPlayer account = playerManager.getPlayerFromHydrion(uuid);

        if (account == null) {
            account = playerManager.createPlayer(false, uuid, name);
        }

        if (uuid.equals(UUID.fromString("bc4da331-e028-4d78-930b-4652dc087642"))) {
            account.getRank().setPlayerType(HyriPlayerRankType.EPIC);
            account.getRank().setStaffType(HyriStaffRankType.ADMINISTRATOR);
        }

        account.setName(name);
        account.setLastLoginDate(new Date(System.currentTimeMillis()));
        account.setCurrentProxy(HyriAPI.get().getProxy().getName());
        account.update();

        if (this.hydrionManager.isEnabled()) {
            this.playerModule.setPlayer(uuid, HyriAPI.GSON.toJson(account));

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

                final IHyriNickname nickname = account.getNickname();

                if (nickname != null) {
                    HyriAPI.get().getPlayerManager().getNicknameManager().removeUsedNickname(nickname.getName());

                    account.setNickname(null);
                }

                result = true;
            }

            account.setOnline(false);
            account.setCurrentProxy(null);

            if (this.hydrionManager.isEnabled()) {
                final IHyriFriendManager friendManager = HyriAPI.get().getFriendManager();

                this.hydrionManager.getClient().getFriendsModule().setFriends(uuid, HyriAPI.GSON.toJson(new HyriFriends(friendManager.getFriends(uuid))));
                this.playerModule.setPlayer(uuid, HyriAPI.GSON.toJson(account));

                friendManager.removeFriends(uuid);
                HyriAPI.get().getPlayerManager().removePlayer(uuid);
            } else {
                account.update();
            }
            return result;
        }
        return false;
    }

}
