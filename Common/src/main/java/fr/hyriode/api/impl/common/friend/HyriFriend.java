package fr.hyriode.api.impl.common.friend;

import fr.hyriode.api.friend.IHyriFriend;

import java.util.Date;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/03/2022 at 15:34
 */
public class HyriFriend implements IHyriFriend {

    private final UUID uuid;
    private final long whenAdded;

    public HyriFriend(UUID uuid) {
        this.uuid = uuid;
        this.whenAdded = System.currentTimeMillis();
    }

    public HyriFriend(UUID uuid, Date whenAdded) {
        this.uuid = uuid;
        this.whenAdded = whenAdded.getTime();
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public Date getWhenAdded() {
        return new Date(this.whenAdded);
    }

}
