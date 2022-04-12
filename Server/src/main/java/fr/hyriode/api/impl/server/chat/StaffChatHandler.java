package fr.hyriode.api.impl.server.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.HyriDefaultChatChannel;
import fr.hyriode.api.chat.IHyriChatChannelHandler;
import fr.hyriode.api.chat.IHyriChatChannelManager;
import fr.hyriode.api.impl.server.util.ChatUtil;
import fr.hyriode.api.rank.EHyriRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StaffChatHandler implements IHyriChatChannelHandler {

    @Override
    public String getChannel() {
        return HyriDefaultChatChannel.STAFF.getChannel();
    }

    @Override
    public EHyriRank getRequiredRank() {
        return EHyriRank.STAFF;
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
        return ChatColor.AQUA + "Staff";
    }

    @Override
    public String getMessageFormat() {
        return "%s: ";
    }

    @Override
    public void onMessage(String channel, String message, UUID sender, boolean force) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!force && IHyriChatChannelManager.canPlayerAccessChannel(channel, HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId()))) {
                continue;
            }

            if (!force && !HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId()).isInModerationMode()) {
                continue;
            }

            this.onMessageToPlayer(channel, player.getUniqueId(), message, sender, force);
        }
    }

    @Override
    public void onMessageToPlayer(String channel, UUID receiver, String message, UUID sender, boolean force) {
        final Player player = Bukkit.getPlayer(receiver);

        if (player == null) {
            Bukkit.getLogger().warning("[StaffChatHandler] Player not found: " + receiver);
            return;
        }

        player.spigot().sendMessage(ChatUtil.formatDefault(this.getPrefix(), this.getMessageFormat(), sender, message).create());
        player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.A));
    }
}
