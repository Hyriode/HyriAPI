package fr.hyriode.hyriapi.player.event;

import fr.hyriode.hyriapi.player.IHyriPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 07:50
 */
public class HyriPlayerCreatedEvent extends Event {

    private final HandlerList handlers = new HandlerList();

    private final IHyriPlayer player;

    public HyriPlayerCreatedEvent(IHyriPlayer player) {
        this.player = player;
    }

    public IHyriPlayer getPlayer() {
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return this.handlers;
    }

}
