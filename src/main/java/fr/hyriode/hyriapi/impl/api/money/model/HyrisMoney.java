package fr.hyriode.hyriapi.impl.api.money.model;

import fr.hyriode.hyriapi.impl.api.money.HyriMoney;
import org.bukkit.ChatColor;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/07/2021 at 22:08
 */
public class HyrisMoney extends HyriMoney {

    public HyrisMoney(UUID playerUUID) {
        super(playerUUID, "Hyris", ChatColor.DARK_PURPLE);
    }

}
