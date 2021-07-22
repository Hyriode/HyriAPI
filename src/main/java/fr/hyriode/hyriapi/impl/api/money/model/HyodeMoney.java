package fr.hyriode.hyriapi.impl.api.money.model;

import fr.hyriode.hyriapi.impl.api.money.HyriMoney;
import org.bukkit.ChatColor;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/07/2021 at 22:30
 */
public class HyodeMoney extends HyriMoney {

    public HyodeMoney(UUID playerUUID) {
        super(playerUUID, "Hyode", ChatColor.AQUA);
    }

}
