package fr.hyriode.api.chat;

import fr.hyriode.api.event.HyriCancellableEvent;
import fr.hyriode.api.event.HyriEvent;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 12/12/2022 at 20:05.<br>
 *
 * Event triggered when a player send a private message to another player.
 */
public class PrivateMessageEvent extends HyriCancellableEvent {

    private final UUID sender;
    private final UUID target;
    private final String message;

    public PrivateMessageEvent(UUID sender, UUID target, String message) {
        this.sender = sender;
        this.target = target;
        this.message = message;
    }

    public UUID getSender() {
        return this.sender;
    }

    public UUID getTarget() {
        return this.target;
    }

    public String getMessage() {
        return this.message;
    }

}
