package fr.hyriode.hyriapi.tab;

import fr.hyriode.hyriapi.util.PacketUtil;
import fr.hyriode.hyriapi.util.reflection.Reflection;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TabTitle {

    public static void setTabTitle(Player player, String header, String footer) {
        if (header == null) header = "";
        if (footer == null) footer = "";

        final PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        final Field a = Reflection.getField(packet.getClass(), "a");
        final Field b = Reflection.getField(packet.getClass(), "b");

        Reflection.setField(a, packet, new ChatComponentText(header));
        Reflection.setField(b, packet, new ChatComponentText(footer));

        PacketUtil.sendPacket(player, packet);
    }

}
