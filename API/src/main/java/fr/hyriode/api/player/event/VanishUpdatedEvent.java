package fr.hyriode.api.player.event;

import fr.hyriode.api.event.HyriEvent;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerSession;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 01/04/2023 at 09:49.<br>
 *
 * This event is triggered each time a {@link IHyriPlayerSession} is changed with a new vanish state.
 */
public class VanishUpdatedEvent extends HyriEvent {

    private IHyriPlayer account;

    private final IHyriPlayerSession session;

    public VanishUpdatedEvent(IHyriPlayerSession session) {
        this.session = session;
    }

    public UUID getPlayerId() {
        return this.session.getPlayerId();
    }

    public boolean isVanished() {
        return this.session.isVanished();
    }

    public IHyriPlayerSession getSession() {
        return this.session;
    }

    public IHyriPlayer getAccount() {
        return this.account != null ? this.account : (this.account = IHyriPlayer.get(this.getPlayerId()));
    }

}
