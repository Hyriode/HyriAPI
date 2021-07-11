package fr.hyriode.hyriapi.tools.sign;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface SignCompleteCallback {

    void call(Player player, BlockPosition blockPosition, String[] lines);

}
