package fr.hyriode.api.impl.server.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.channel.HyriChatChannel;
import fr.hyriode.api.chat.channel.IHyriChatChannelManager;
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

public class StaffChatHandler extends CommonChatHandler {

    public StaffChatHandler() {
        super(ChatColor.AQUA + "Staff » ");
    }

    @Override
    public String getChannel() {
        return HyriChatChannel.STAFF.getChannel();
    }

    @Override
    public HyriStaffRankType getRequiredStaffRank() {
        return HyriStaffRankType.HELPER;
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
    public void onMessage(String channel, String message, UUID sender, boolean force) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!IHyriChatChannelManager.canPlayerAccessChannel(channel, HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId())) && !force) {
                continue;
            }

            final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(sender);
            final IHyriNickname nickname = account.getNickname();
            final ComponentBuilder builder = new ComponentBuilder(this.prefix)
                    .append(account.getNameWithRank())
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("")
                            .append(ChatColor.AQUA + "Nickname: " + (nickname != null ? ChatColor.WHITE + nickname.getName() : ChatColor.RED + "✘") + "\n")
                            .append(ChatColor.AQUA + "In moderation: " + (account.isInModerationMode() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No") + "\n")
                            .append(ChatColor.AQUA + "In vanish: " + (account.isInVanishMode() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No") + "\n")
                            .append(ChatColor.AQUA + "On server: " + ChatColor.WHITE + account.getCurrentServer() + "\n")
                            .append(ChatColor.AQUA + "Click to " + ChatColor.GOLD + "teleport" + ChatColor.AQUA + " on his server.").create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyriserver " + account.getCurrentServer()))
                    .append(ChatColor.WHITE + ": " + message)
                    .event((ClickEvent) null).event((HoverEvent) null);

            player.spigot().sendMessage(builder.create());
        }
    }

}
