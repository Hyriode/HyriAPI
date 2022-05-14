package fr.hyriode.api.impl.server.join;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriendManager;
import fr.hyriode.api.impl.common.friend.HyriFriends;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.impl.common.player.packet.HyriPlayerLeavePacket;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.nickname.IHyriNickname;
import fr.hyriode.api.rank.HyriPlus;
import fr.hyriode.api.rank.HyriRank;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import fr.hyriode.hydrion.client.HydrionClient;
import fr.hyriode.hydrion.client.module.FriendsModule;
import fr.hyriode.hydrion.client.module.PlayerModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Date;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by Corentin
 * on 17/02/2022 at 18:36
 */
public class HyriJoinListener implements Listener, IHyriPacketReceiver {

    private final Queue<UUID> disconnectedFromProxy;

    private final IHyriFriendManager friendManager;

    private final HyggdrasilManager hyggdrasilManager;
    private final HydrionManager hydrionManager;

    private final HyriJoinManager joinManager;

    private FriendsModule friendsModule;

    public HyriJoinListener(HyggdrasilManager hyggdrasilManager, HydrionManager hydrionManager, HyriJoinManager joinManager) {
        this.hyggdrasilManager = hyggdrasilManager;
        this.hydrionManager = hydrionManager;
        this.joinManager = joinManager;
        this.friendManager = HyriAPI.get().getFriendManager();
        this.disconnectedFromProxy = new ConcurrentLinkedQueue<>();

        if (this.hydrionManager.isEnabled()) {
            this.friendsModule = this.hydrionManager.getClient().getFriendsModule();
        }

        HyriAPI.get().getPubSub().subscribe(HyriChannel.SERVERS, this);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        try {
            final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
            final UUID uuid = event.getUniqueId();

            if (playerManager.getPlayer(uuid) == null) {
                playerManager.createPlayer(true, uuid, event.getName());
            }

            this.joinManager.onLogin(event);
        } catch (Exception e) {
            e.printStackTrace();
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "An error occurred while loading your profile!");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final IHyriPlayer account = playerManager.getPlayerFromRedis(uuid);

        if (account == null) {
            player.kickPlayer(ChatColor.RED + "An error occurred while loading your profile!");
            return;
        }

        event.setJoinMessage("");

        if (HyriAPI.get().getConfiguration().isDevEnvironment()) {
            account.setName(player.getName());
            account.setLastLoginDate(new Date(System.currentTimeMillis()));

            playerManager.setPlayerId(account.getName(), uuid);

            this.friendManager.saveFriends(this.friendManager.createHandler(uuid));
        }

        if (account.getRank().is(HyriStaffRankType.ADMINISTRATOR)) {
            player.setOp(true);
        }

        account.setLastServer(account.getCurrentServer());
        account.setOnline(true);
        account.setCurrentServer(HyriAPI.get().getServer().getName());

        if (this.hydrionManager.isEnabled()) {
            this.updateAccountFromHydrion(account, IHyriPlayer::update);
        }

        playerManager.savePrefix(uuid, account.getPrefix());

        final IHyriParty party = HyriAPI.get().getPartyManager().getParty(account.getParty());

        if (party != null) {
            party.setServer(account.getCurrentServer());
        }

        this.joinManager.onJoin(player);

        HyriAPI.get().getServer().addPlayer(player.getUniqueId());

        this.hyggdrasilManager.sendData();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");

        this.onLeave(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerKicked(PlayerKickEvent event) {
        event.setLeaveMessage("");

        this.onLeave(event.getPlayer());
    }

    private void updateAccountFromHydrion(IHyriPlayer account, Consumer<IHyriPlayer> accountConsumer) {
        final UUID uuid = account.getUniqueId();

        HyriAPI.get().getPlayerManager().getPlayerFromHydrion(uuid).whenComplete((hydrionPlayer, throwable) -> {
            if (hydrionPlayer != null) {
                final HyriRank rank = hydrionPlayer.getRank();

                if (rank.isSuperior(account.getRank().getPlayerType())) {
                    account.setRank(rank);
                }

                final HyriPlus hyriPlus = hydrionPlayer.getHyriPlus();

                if (hyriPlus.getExpirationDate().getTime() > account.getHyriPlus().getExpirationDate().getTime()) {
                    account.setHyriPlus(hyriPlus);
                }
            }

            accountConsumer.accept(account);

            HyriAPI.get().getPlayerManager().sendPlayerToHydrion(account);
        });
    }

    private void onLeave(Player player) {
        final UUID uuid = player.getUniqueId();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final IHyriPlayer account = playerManager.getPlayerFromRedis(player.getUniqueId());

        if (account != null) {
            if (HyriAPI.get().getConfiguration().isDevEnvironment()) {
                account.setPlayTime(account.getPlayTime() + (System.currentTimeMillis() - account.getLastLoginDate().getTime()));
                account.setOnline(false);
                account.setLastPrivateMessagePlayer(null);

                final IHyriNickname nickname = account.getNickname();

                if (nickname != null) {
                    playerManager.getNicknameManager().removeUsedNickname(nickname.getName());

                    account.setNickname(null);
                }

                account.update();
            }

            if (this.hydrionManager.isEnabled()) {
                this.friendsModule.setFriends(uuid, HyriAPI.GSON.toJson(new HyriFriends(this.friendManager.getFriends(uuid))));
            }

            if (this.disconnectedFromProxy.remove(uuid)) {
                this.updateAccountFromHydrion(account, newAccount -> {
                    playerManager.removePlayer(uuid);

                    HyriAPI.get().getFriendManager().removeFriends(uuid);
                });
            }
        }

        this.joinManager.onLogout(player);

        HyriAPI.get().getServer().removePlayer(uuid);

        this.hyggdrasilManager.sendData();
    }

    @Override
    public void receive(String channel, HyriPacket packet) {
        if (packet instanceof HyriPlayerLeavePacket) {
            final HyriPlayerLeavePacket leavePacket = (HyriPlayerLeavePacket) packet;
            final UUID playerId = leavePacket.getPlayer();
            final Player player = Bukkit.getPlayer(playerId);

            if (player != null) {
                this.disconnectedFromProxy.add(playerId);
            }
        }
    }

}
