package fr.hyriode.api.impl.server.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.channel.HyriChatChannel;
import fr.hyriode.api.party.HyriPartyRank;
import fr.hyriode.api.party.IHyriParty;
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

public class PartyChatHandler extends CommonChatHandler {

    public PartyChatHandler() {
        super(ChatColor.DARK_AQUA + "Party » ");
    }

    @Override
    public String getChannel() {
        return HyriChatChannel.PARTY.getChannel();
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
    public void onMessage(String channel, String message, UUID sender, boolean force) {
        final IHyriParty party = HyriAPI.get().getPartyManager().getPlayerParty(sender);

        if (party == null) {
            final Player player = Bukkit.getPlayer(sender);

            if (player != null) {
                player.sendMessage(ChatColor.RED + "You need to be in a party to talk in this chat!");
            }
            return;
        }

        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(sender);
        final HyriPartyRank rank = party.getRank(sender);

        for (UUID member : party.getMembers().keySet()) {
            final Player player = Bukkit.getPlayer(member);

            if (player == null) {
                continue;
            }

            final ComponentBuilder builder = new ComponentBuilder(this.prefix)
                    .append(account.getNameWithRank())
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("")
                            .append(ChatColor.AQUA + "Server: " + ChatColor.WHITE + account.getCurrentServer() + "\n")
                            .append(ChatColor.AQUA + "Rank: " + ChatColor.WHITE + rank.getDisplay(HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId()).getSettings().getLanguage()))
                            .create()))
                    .append(ChatColor.WHITE + ": " + message)
                    .event((ClickEvent) null).event((HoverEvent) null);

            player.spigot().sendMessage(builder.create());
        }
    }

}
