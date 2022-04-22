package fr.hyriode.api.impl.server.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.HyriChatChannel;
import fr.hyriode.api.chat.IHyriChatChannelHandler;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GlobalChatHandler implements IHyriChatChannelHandler {

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

        if (!force && account.isInVanishMode()) {
            Bukkit.getPlayer(sender).spigot().sendMessage(new ComponentBuilder("")
                    .append(ChatColor.YELLOW + "/!\\" + ChatColor.RED + " You are about to send a message in global chat,\n")
                    .event((ClickEvent) null).event((HoverEvent) null)
                    .append(ChatColor.RED + "but you are vanished. Do you want to send it anyway ? ").bold(false)
                    .append(ChatColor.GREEN + " [Send] ").bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/forcemsg " + this.getChannel() + " " + message))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "Click to send the message").create())).create());
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!account.getSettings().isGlobalChatMessagesEnabled() && !force) {
                continue;
            }

            player.sendMessage(account.getNameWithRank(true) + ChatColor.WHITE + ": " + (account.getRank().isDefault() ? ChatColor.GRAY : ChatColor.WHITE) + message);
        }
    }

}
