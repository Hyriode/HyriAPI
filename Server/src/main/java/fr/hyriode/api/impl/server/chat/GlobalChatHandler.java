package fr.hyriode.api.impl.server.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.HyriDefaultChatChannel;
import fr.hyriode.api.chat.IHyriChatChannelHandler;
import fr.hyriode.api.impl.server.util.ChatUtil;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.rank.EHyriRank;
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
        return HyriDefaultChatChannel.GLOBAL.getChannel();
    }

    @Override
    public EHyriRank getRequiredRank() {
        return EHyriRank.PLAYER;
    }

    @Override
    public boolean isAcrossNetwork() {
        return false;
    }

    @Override
    public boolean canBeJoined() {
        return true;
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public String getMessageFormat() {
        return "%s: ";
    }

    @Override
    public void onMessage(String channel, String message, UUID sender, boolean force) {
        final IHyriPlayerManager manager = HyriAPI.get().getPlayerManager();
        final IHyriPlayer account = sender != null ? HyriAPI.get().getPlayerManager().getPlayer(sender) : null;

        if (!force && account != null && account.isInVanishMode()) {
            Bukkit.getPlayer(sender).spigot().sendMessage(new ComponentBuilder("")
                    .append(ChatColor.YELLOW + "/!\\" + ChatColor.RED + " You are about to send a message in global chat,\n")
                    .event((ClickEvent) null).event((HoverEvent) null)
                    .append(ChatColor.RED + "but you are vanished. Do you want to send it anyway ? ").bold(false)
                    .append(ChatColor.GREEN + " [Send] ").bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat " + this.getChannel() + " " + true + " " + message))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to send the message").create())).create());
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!force && !manager.getPlayer(player.getUniqueId()).getSettings().isGlobalChatMessagesEnabled()) {
                continue;
            }

            this.onMessageToPlayer(channel, player.getUniqueId(), message, sender, force);
        }
    }

    @Override
    public void onMessageToPlayer(String channel, UUID receiver, String message, UUID sender, boolean force) {
        final Player player = Bukkit.getPlayer(receiver);

        if (player == null) {
            Bukkit.getLogger().warning("[GlobalChatHandler] Player not found: " + receiver);
            return;
        }

        player.spigot().sendMessage(ChatUtil.formatDefault(this.getPrefix(), this.getMessageFormat(), sender, message).create());
    }
}
