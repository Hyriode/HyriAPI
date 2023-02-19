package fr.hyriode.api.impl.common.game.rotating;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.game.rotating.IHyriRotatingGame;
import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.ObjectDataInput;
import fr.hyriode.api.serialization.ObjectDataOutput;

import java.io.IOException;

/**
 * Created by AstFaster
 * on 27/07/2022 at 12:07
 */
public class HyriRotatingGame implements IHyriRotatingGame, DataSerializable {

    private String gameName;
    private long sinceWhen;

    public HyriRotatingGame() {}

    public HyriRotatingGame(String gameName, long sinceWhen) {
        this.gameName = gameName;
        this.sinceWhen = sinceWhen;
    }

    @Override
    public void write(ObjectDataOutput output) throws IOException {
        output.writeString(this.gameName);
        output.writeLong(this.sinceWhen);
    }

    @Override
    public void read(ObjectDataInput input) throws IOException {
        this.gameName = input.readString();
        this.sinceWhen = input.readLong();
    }

    @Override
    public IHyriGameInfo getInfo() {
        return HyriAPI.get().getGameManager().getGameInfo(this.gameName);
    }

    @Override
    public long sinceWhen() {
        return this.sinceWhen;
    }

}
