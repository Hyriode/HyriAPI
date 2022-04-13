package fr.hyriode.api.impl.server.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.HyriDefaultChatChannel;
import fr.hyriode.api.chat.IHyriChatChannelHandler;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PrivateChatHandler implements IHyriChatChannelHandler {

    @Override
    public String getChannel() {
        return HyriDefaultChatChannel.PRIVATE.getChannel();
    }

    @Override
    public HyriStaffRankType getRequiredStaffRank() {
        return null;
    }

    @Override
    public HyriPlayerRankType getRequiredPlayerRank() {
        return null;
    }

    @Override
    public boolean isAcrossNetwork() {
        return true;
    }

    @Override
    public boolean canBeJoined() {
        return false;
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public String getMessageFormat() {
        return ChatColor.AQUA + "[" + ChatColor.WHITE + "%s" + " → " + "%s" + ChatColor.AQUA + "] " + ChatColor.WHITE + "%s";
    }

    @Override
    public void onMessage(String channel, String message, UUID sender, boolean force) {}

    @Override
    public void onMessageToPlayer(String channel, UUID receiverID, String message, UUID senderID, boolean force) {
        final IHyriPlayerManager manager = HyriAPI.get().getPlayerManager();
        final Player player = Bukkit.getPlayer(receiverID);

        if (player == null) {
            Bukkit.getLogger().warning("[PrivateChatHandler] Player not found: " + receiverID);
            return;
        }

        final IHyriPlayer sender = manager.getPlayer(senderID);
        final IHyriPlayer receiver = manager.getPlayer(receiverID);

        if (!force) {
            switch (receiver.getSettings().getPrivateMessagesLevel()) {
                case NONE:
                    return;
                case FRIENDS:
                    if (HyriAPI.get().getFriendManager().loadFriends(senderID).areFriends(receiverID)) {
                        break;
                    }
                case ALL:
                    break;
            }
        }

        player.spigot().sendMessage(new ComponentBuilder("").append(String.format(this.getMessageFormat(), sender.getNameWithRank(), receiver.getName(), message))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.AQUA + "Click to reply to " + receiver.getName()).create()))
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/r ")).create());
    }

}
