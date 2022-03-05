package fr.hyriode.api.impl.common.money;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class Hyris extends HyriMoney {

    public Hyris(UUID playerUUID) {
        super(playerUUID, "Hyris", '6');
    }

}
