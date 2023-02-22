package fr.hyriode.api.impl.common.game;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.game.IHyriGameType;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;

import java.io.IOException;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 17/04/2022 at 20:52
 */
public class HyriGameType implements IHyriGameType, DataSerializable {

    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private String displayName;

    public HyriGameType() {}

    public HyriGameType(int id, String name, String displayName) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeInt(this.id);
        output.writeString(this.name);
        output.writeString(this.displayName);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.id = input.readInt();
        this.name = input.readString();
        this.displayName = input.readString();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

}
