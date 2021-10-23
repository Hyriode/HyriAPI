package fr.hyriode.hyriapi.cosmetics;

import fr.hyriode.hyriapi.player.IHyriPlayer;

public interface IHyriCosmeticTask extends Runnable {

    void setPlayer(IHyriPlayer player);
}
