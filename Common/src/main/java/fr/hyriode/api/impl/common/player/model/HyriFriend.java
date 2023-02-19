package fr.hyriode.api.impl.common.player.model;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.player.model.IHyriFriend;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 18/02/2023 at 10:01
 */
public class HyriFriend implements IHyriFriend, MongoSerializable, DataSerializable {

    @Expose
    private UUID uuid;
    @Expose
    private long whenAdded;

    public HyriFriend() {}

    public HyriFriend(UUID uuid, long whenAdded) {
        this.uuid = uuid;
        this.whenAdded = whenAdded;
    }

    @Override
    public void save(MongoDocument document) {
        document.append("uuid", this.uuid.toString());
        document.append("added_date", this.whenAdded);
    }

    @Override
    public void load(MongoDocument document) {
        this.uuid = UUID.fromString(document.getString("uuid"));
        this.whenAdded = document.getLong("added_date");
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeUUID(this.uuid);
        output.writeLong(this.whenAdded);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.uuid = input.readUUID();
        this.whenAdded = input.readLong();
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public long whenAdded() {
        return this.whenAdded;
    }

}
