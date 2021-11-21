package fr.hyriode.hyriapi.impl.money.model;

import fr.hyriode.hyriapi.impl.money.HyriMoney;
import org.bukkit.ChatColor;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyodeMoney extends HyriMoney {

    public HyodeMoney(UUID playerUUID) {
        super(playerUUID, "Hyode", ChatColor.BLUE);
    }

}
