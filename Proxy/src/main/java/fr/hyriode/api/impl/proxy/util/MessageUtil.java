package fr.hyriode.api.impl.proxy.util;

import fr.hyriode.api.HyriConstants;
import fr.hyriode.api.network.IHyriMaintenance;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 20/04/2022 at 10:13
 */
public class MessageUtil {

    public static final BaseComponent[] SERVER_FULL_MESSAGE = createKickMessage(new ComponentBuilder("Le serveur est plein !").color(ChatColor.RED)
            .append("\n\n")
            .append("Achetez un grade sur la ").color(ChatColor.WHITE)
            .append("boutique").color(ChatColor.AQUA)
            .append(" pour être prioritaire.").color(ChatColor.WHITE)
            .append("\n\n")
            .append("Boutique: ").append(HyriConstants.STORE_WEBSITE_URL).color(ChatColor.AQUA)
            .create(), false);

    public static final BaseComponent[] NO_LOBBY_MESSAGE = createKickMessage(new ComponentBuilder("Aucun lobby n'est actuellement démarré !").color(ChatColor.RED)
            .create(), true);

    private static final String HYPHENS = "----------------------";

    public static BaseComponent[] createKickMessage(BaseComponent[] message, boolean emptyLineBetweenSupport) {
        return new ComponentBuilder(HYPHENS).color(ChatColor.AQUA).strikethrough(true)
                .append(" Hyriode ").reset().color(ChatColor.DARK_AQUA)
                .append(HYPHENS).color(ChatColor.AQUA).strikethrough(true)
                .append("\n\n")
                .reset()
                .append(message)
                .append(emptyLineBetweenSupport ? "\n\n" : "")
                .reset()
                .append("Support: ").append(HyriConstants.DISCORD_URL).color(ChatColor.AQUA)
                .append("\n\n")
                .append("----------------------------------------------------").color(ChatColor.AQUA).strikethrough(true)
                .create();
    }

    public static BaseComponent[] createMaintenanceMessage(IHyriMaintenance maintenance) {
        final String reason = maintenance.getReason();

        return createKickMessage(new ComponentBuilder("Une maintenance est actuellement en cours." + (reason != null ? reason : "")).color(ChatColor.RED).create(), true);
    }

}