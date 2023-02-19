package fr.hyriode.api.impl.common.player.model;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.player.model.HyriFriendRequest;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 18/02/2023 at 10:09
 */
public class HyriFriendRequestImpl extends HyriFriendRequest implements DataSerializable {

    @Expose
    private UUID sender;
    @Expose
    private UUID target;

    public HyriFriendRequestImpl() {}

    public HyriFriendRequestImpl(UUID sender, UUID target) {
        this.sender = sender;
        this.target = target;
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeUUID(this.sender);
        output.writeUUID(this.target);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.sender = input.readUUID();
        this.target = input.readUUID();
    }

    @Override
    public UUID getSender() {
        return this.sender;
    }

    @Override
    public UUID getTarget() {
        return this.target;
    }

}
