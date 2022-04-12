package fr.hyriode.api.impl.server.util;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.EHyriRank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

import java.util.Arrays;
import java.util.UUID;

public class ChatUtil {

    public static ComponentBuilder formatDefault(String prefix, String format, UUID sender, String message) {
        final IHyriPlayer account = sender != null ? HyriAPI.get().getPlayerManager().getPlayer(sender) : null;
        final boolean hasPlayerRank = account != null && account.getRank().getType() == EHyriRank.PLAYER;
        final String rank = account != null ? account.getNameWithRank() : null;
        final ChatColor color = hasPlayerRank ? ChatColor.GRAY : ChatColor.WHITE;
        final String channel = prefix != null ? prefix.substring(2).toLowerCase() : null;

        final ComponentBuilder builder = new ComponentBuilder("");

        if (prefix != null) {
            builder.append(prefix);

            builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("The " + prefix + ChatColor.WHITE + " chat, click to join it.").create()));
            builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat " + channel));

            builder.append(" » ").event((ClickEvent) null).event((HoverEvent) null);
        }

        builder.append(String.format(format, (hasPlayerRank ? color : "") + rank));

        if (account != null && prefix != null && prefix.contains("Staff")) {
            builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("")
                    .append(ChatColor.AQUA + "In moderation: " + (account.isInModerationMode() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No") + "\n")
                    .append(ChatColor.AQUA + "In vanish: " + (account.isInVanishMode() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No") + "\n")
                    .append(ChatColor.AQUA + "On server: " + ChatColor.WHITE + account.getCurrentServer() + "\n")
                    .append(ChatColor.AQUA + "Click to " + ChatColor.GOLD + "teleport" + ChatColor.AQUA + " on his server.").create()));
            builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyriserver " + account.getCurrentServer()));
        }

        return builder.append(color + message).event((ClickEvent) null).event((HoverEvent) null);
    }
}
