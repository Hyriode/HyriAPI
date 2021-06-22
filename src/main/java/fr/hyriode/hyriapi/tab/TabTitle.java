package fr.hyriode.hyriapi.tab;

import fr.hyriode.hyriapi.util.PacketUtil;
import fr.hyriode.hyriapi.util.Reflection;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TabTitle {

    public static void setTabTitle(Player player, String header, String footer) {
        if (header == null) header = "";
        if (footer == null) footer = "";

        try {
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            final Field a = packet.getClass().getDeclaredField("a");
            final Field b = packet.getClass().getDeclaredField("b");

            Reflection.setField(a, packet, new ChatComponentText(header));
            Reflection.setField(b, packet, new ChatComponentText(footer));

            PacketUtil.sendPacket(player, packet);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

}
