package fr.hyriode.api.impl.server.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.HyriDefaultChatChannel;
import fr.hyriode.api.chat.IHyriChatChannelHandler;
import fr.hyriode.api.chat.IHyriChatChannelManager;
import fr.hyriode.api.impl.server.util.ChatUtil;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PartnerChatHandler implements IHyriChatChannelHandler {

    @Override
    public String getChannel() {
        return HyriDefaultChatChannel.PARTNER.getChannel();
    }

    @Override
    public HyriStaffRankType getRequiredStaffRank() {
        return HyriStaffRankType.HELPER;
    }

    @Override
    public HyriPlayerRankType getRequiredPlayerRank() {
        return HyriPlayerRankType.PARTNER;
    }

    @Override
    public boolean isAcrossNetwork() {
        return true;
    }

    @Override
    public boolean canBeJoined() {
        return true;
    }

    @Override
    public String getPrefix() {
        return ChatColor.GOLD + "Partner";
    }

    @Override
    public String getMessageFormat() {
        return "%s: ";
    }

    @Override
    public void onMessage(String channel, String message, UUID sender, boolean force) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (IHyriChatChannelManager.canPlayerAccessChannel(channel, HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId()))) {
                continue;
            }

            this.onMessageToPlayer(channel, player.getUniqueId(), message, sender, force);
        }
    }

    @Override
    public void onMessageToPlayer(String channel, UUID receiver, String message, UUID sender, boolean force) {
        final Player player = Bukkit.getPlayer(receiver);

        if (player == null) {
            Bukkit.getLogger().warning("[PartnerChatHandler] Player not found: " + receiver);
            return;
        }

        player.spigot().sendMessage(ChatUtil.formatDefault(this.getPrefix(), this.getMessageFormat(), sender, message).create());
    }

}
