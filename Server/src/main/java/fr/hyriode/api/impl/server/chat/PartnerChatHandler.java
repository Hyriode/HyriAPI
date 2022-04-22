package fr.hyriode.api.impl.server.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.HyriChatChannel;
import fr.hyriode.api.chat.IHyriChatChannelHandler;
import fr.hyriode.api.chat.IHyriChatChannelManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.nickname.IHyriNickname;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PartnerChatHandler implements IHyriChatChannelHandler {

    @Override
    public String getChannel() {
        return HyriChatChannel.PARTNER.getChannel();
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
    public void onMessage(String channel, String message, UUID sender, boolean force) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!IHyriChatChannelManager.canPlayerAccessChannel(channel, HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId()))) {
                continue;
            }

            final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(sender);
            final ComponentBuilder builder = new ComponentBuilder(ChatColor.GOLD + "Partner » ")
                    .append(account.getNameWithRank());

            if (account.getRank().isStaff()) {
                final IHyriNickname nickname = account.getNickname();

                builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("")
                                .append(ChatColor.AQUA + "Nickname: " + (nickname != null ? ChatColor.WHITE + nickname.getName() : ChatColor.RED + "✘") + "\n")
                                .append(ChatColor.AQUA + "On server: " + ChatColor.WHITE + account.getCurrentServer() + "\n")
                                .append(ChatColor.AQUA + "Click to " + ChatColor.GOLD + "teleport" + ChatColor.AQUA + " on his server.").create()))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyriserver " + account.getCurrentServer()));
            }

            builder.append(ChatColor.WHITE + ": " + message).event((ClickEvent) null).event((HoverEvent) null);

            player.spigot().sendMessage(builder.create());
        }
    }

}
