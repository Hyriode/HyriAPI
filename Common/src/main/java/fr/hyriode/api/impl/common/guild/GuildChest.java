package fr.hyriode.api.impl.common.guild;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.guild.IGuildChest;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;

import java.io.IOException;

/**
 * Created by AstFaster
 * on 13/02/2023 at 22:15
 */
public class GuildChest implements IGuildChest, MongoSerializable, DataSerializable {

    @Expose
    private long hyris;

    @Override
    public void load(MongoDocument document) {
        this.hyris = document.getLong("hyris");
    }

    @Override
    public void save(MongoDocument document) {
        document.append("hyris", this.hyris);
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeLong(this.hyris);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.hyris = input.readLong();
    }

    @Override
    public long getHyris() {
        return this.hyris;
    }

    @Override
    public void addHyris(long hyris) {
        if (hyris <= 0) {
            throw new IllegalArgumentException("Hyris amount must be greater than 0!");
        }

        this.hyris += hyris;
    }

    @Override
    public void removeHyris(long hyris) {
        if (hyris <= 0) {
            throw new IllegalArgumentException("Hyris amount must be greater than 0!");
        }

        this.hyris -= hyris;
    }

}
