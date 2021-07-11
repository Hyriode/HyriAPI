package fr.hyriode.hyriapi.tools.npc;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface NPCInteractCallback {

    void call(boolean rightClick, Player player);

}
