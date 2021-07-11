package fr.hyriode.hyriapi.tools.title;

import fr.hyriode.hyriapi.util.PacketUtil;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.entity.Player;

public class Title {

    public static void setTitle(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        PacketUtil.sendPacket(player, new PacketPlayOutTitle(fadeIn, stay, fadeOut));

        if (title != null) {
            PacketUtil.sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatComponentText(title)));
        }
        if (subTitle != null) {
            PacketUtil.sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, new ChatComponentText(subTitle)));
        }
    }

}