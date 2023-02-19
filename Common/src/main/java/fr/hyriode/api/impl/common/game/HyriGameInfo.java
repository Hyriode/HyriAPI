package fr.hyriode.api.impl.common.game;

import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.game.IHyriGameType;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 17/04/2022 at 20:54
 */
public class HyriGameInfo implements IHyriGameInfo, DataSerializable {

    private String name;
    private String displayName;
    private final List<IHyriGameType> types = new ArrayList<>();

    public HyriGameInfo() {}

    public HyriGameInfo(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeString(this.name);
        output.writeString(this.displayName);
        output.writeInt(this.types.size());

        for (IHyriGameType type : this.types) {
            ((HyriGameType) type).write(output);
        }
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.name = input.readString();
        this.displayName = input.readString();

        final int size = input.readInt();

        for (int i = 0; i < size; i++) {
            final HyriGameType type = new HyriGameType();

            type.read(input);

            this.types.add(type);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public IHyriGameType getType(String name) {
        for (IHyriGameType type : this.types) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public void addType(int id, String name, String displayName) {
        this.types.add(new HyriGameType(id, name, displayName));
    }

    @Override
    public void removeType(String name) {
        final IHyriGameType type = this.getType(name);

        if (type != null) {
            this.types.remove(type);
        }
    }

    @Override
    public List<IHyriGameType> getTypes() {
        return this.types;
    }


}
