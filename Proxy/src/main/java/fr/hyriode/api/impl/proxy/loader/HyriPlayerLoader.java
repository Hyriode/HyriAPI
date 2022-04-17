package fr.hyriode.api.impl.proxy.loader;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.api.party.HyriPartyDisbandReason;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.hydrion.client.module.PlayerModule;

import java.util.Date;
import java.util.UUID;

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

    public void loadPlayerAccount(UUID uuid, String name) {
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();

        IHyriPlayer account = playerManager.getPlayer(uuid);

        if (account == null) {
            account = playerManager.createPlayer(true, uuid, name);
        }

        account.setName(name);
        account.setLastLoginDate(new Date(System.currentTimeMillis()));
        account.setOnline(true);
        account.setCurrentProxy(HyriAPI.get().getProxy().getName());
        account.update();

        if (this.hydrionManager.isEnabled()) {
            this.playerModule.setPlayer(uuid, HyriAPI.GSON.toJson(account));
        }

        playerManager.setPlayerId(name, uuid);
    }

    public void unloadPlayerAccount(UUID uuid) {
        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(uuid);

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

            account.setPlayTime(account.getPlayTime() + (System.currentTimeMillis() - account.getLastLoginDate().getTime()));
            account.setOnline(false);
            account.setCurrentProxy(null);
            account.setParty(null);

            if (this.hydrionManager.isEnabled()) {
                HyriAPI.get().getPlayerManager().removePlayer(account.getUniqueId());

                this.playerModule.setPlayer(account.getUniqueId(), HyriAPI.GSON.toJson(account));
            } else {
                account.update();
            }
        }
    }

}
