package fr.hyriode.api.impl.server.chat;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.chat.channel.IHyriChatChannelHandler;
import fr.hyriode.api.chat.channel.IHyriChatChannelManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 30/04/2022 at 13:07
 */
public abstract class CommonChatHandler implements IHyriChatChannelHandler {

    protected final String prefix;

    public CommonChatHandler(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void onComponent(String channel, String component, boolean force) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!IHyriChatChannelManager.canPlayerAccessChannel(channel, HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId())) && !force) {
                continue;
            }

            final TextComponent message = new TextComponent(this.prefix);

            message.addExtra(new TextComponent(ComponentSerializer.parse(component)));

            player.spigot().sendMessage(message);
        }
    }

}
