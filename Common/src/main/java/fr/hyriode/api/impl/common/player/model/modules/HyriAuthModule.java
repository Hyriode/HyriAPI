package fr.hyriode.api.impl.common.player.model.modules;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.player.model.modules.IHyriAuthModule;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

/**
 * Created by AstFaster
 * on 08/01/2023 at 14:11
 */
public class HyriAuthModule implements IHyriAuthModule, MongoSerializable, DataSerializable {

    @Expose
    private boolean premium;
    @Expose
    private String hash;

    public HyriAuthModule() {}

    public HyriAuthModule(boolean premium) {
        this.premium = premium;
    }

    @Override
    public void save(MongoDocument document) {
        document.append("premium", this.premium);
        document.append("hash", this.hash);
    }

    @Override
    public void load(MongoDocument document) {
        this.premium = document.getBoolean("premium");
        this.hash = document.getString("hash");
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeBoolean(this.premium);
        output.writeString(this.hash);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.premium = input.readBoolean();
        this.hash = input.readString();
    }

    @Override
    public boolean isPremium() {
        return this.premium;
    }

    @Override
    public void setPremium(boolean premium) {
        this.premium = premium;
        this.hash = null;
    }

    @Override
    public @NotNull String getHash() {
        return this.hash;
    }

    @Override
    public void newHash(@NotNull String password) {
        this.hash = BCrypt.hashpw(password, BCrypt.gensalt(6));
    }

    @Override
    public boolean authenticate(@NotNull String password) {
        return BCrypt.checkpw(password, this.hash);
    }

}
