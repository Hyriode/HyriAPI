package fr.hyriode.api.impl.server.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.channel.HyriChatChannel;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import fr.hyriode.api.settings.HyriSettingsLevel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GlobalChatHandler extends CommonChatHandler {

    public GlobalChatHandler() {
        super("");
    }

    @Override
    public String getChannel() {
        return HyriChatChannel.GLOBAL.getChannel();
    }

    @Override
    public HyriStaffRankType getRequiredStaffRank() {
        return null;
    }

    @Override
    public HyriPlayerRankType getRequiredPlayerRank() {
        return HyriPlayerRankType.PLAYER;
    }

    @Override
    public boolean isAcrossNetwork() {
        return false;
    }

    @Override
    public void onMessage(String channel, String message, UUID sender, boolean force) {
        if (sender == null) {
            return;
        }

        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(sender);

        if (account == null) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            final UUID playerId = player.getUniqueId();
            final HyriSettingsLevel level = IHyriPlayer.get(playerId).getSettings().getGlobalChatLevel();

            if (level == HyriSettingsLevel.NONE && !force) {
                continue;
            } else if (level == HyriSettingsLevel.FRIENDS && !HyriAPI.get().getFriendManager().createHandler(playerId).areFriends(account.getUniqueId()) && !force) {
                continue;
            }

            if (account.hasNickname()) {
                player.sendMessage(account.getNameWithRank(true) + ChatColor.WHITE + ": " + (account.getNickname().getRank() == HyriPlayerRankType.PLAYER ? ChatColor.GRAY : ChatColor.WHITE) + message);
            } else {
                player.sendMessage(account.getNameWithRank() + (account.getRank().isDefault() ? ChatColor.GRAY : ChatColor.WHITE) + ": " + message);
            }
        }
    }

}
