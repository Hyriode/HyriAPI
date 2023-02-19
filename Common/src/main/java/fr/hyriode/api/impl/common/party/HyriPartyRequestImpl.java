package fr.hyriode.api.impl.common.party;

import fr.hyriode.api.party.HyriPartyRequest;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class HyriPartyRequestImpl extends HyriPartyRequest implements DataSerializable {

    private UUID partyId;
    private UUID sender;
    private UUID target;

    public HyriPartyRequestImpl() {}

    public HyriPartyRequestImpl(UUID partyId, UUID sender, UUID target) {
        this.partyId = partyId;
        this.sender = sender;
        this.target = target;
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeUUID(this.partyId);
        output.writeUUID(this.sender);
        output.writeUUID(this.target);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.partyId = input.readUUID();
        this.sender = input.readUUID();
        this.target = input.readUUID();
    }

    @Override
    public @NotNull UUID getPartyId() {
        return this.partyId;
    }

    @Override
    public @NotNull UUID getSender() {
        return this.sender;
    }

    @Override
    public @NotNull UUID getTarget() {
        return this.target;
    }

}
