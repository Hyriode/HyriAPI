package fr.hyriode.api.impl.server.chat;

import fr.hyriode.api.chat.HyriDefaultChatChannel;
import fr.hyriode.api.chat.IHyriChatChannelHandler;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PluginChatHandler implements IHyriChatChannelHandler {

    @Override
    public String getChannel() {
        return HyriDefaultChatChannel.PLUGIN.getChannel();
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
        return false;
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
        return null;
    }

    @Override
    public void onMessage(String channel, String message, UUID sender, boolean force) {}

    @Override
    public void onMessageToPlayer(String channel, UUID receiver, String message, UUID sender, boolean force) {
        final Player player = Bukkit.getPlayer(receiver);

        if (player == null) {
            Bukkit.getLogger().warning("[PluginChatHandler] Player not found: " + receiver);
            return;
        }

        player.sendMessage(message);
    }

}
