package fr.hyriode.hyriapi.npc;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface NPCInteractCallback {

    void call(boolean rightClick, Player player);

}
