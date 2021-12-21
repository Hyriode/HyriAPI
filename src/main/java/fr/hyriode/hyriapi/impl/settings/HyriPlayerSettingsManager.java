package fr.hyriode.hyriapi.impl.settings;

import fr.hyriode.hyriapi.impl.HyriAPIPlugin;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.settings.*;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public record HyriPlayerSettingsManager(HyriAPIPlugin plugin) implements IHyriPlayerSettingsManager {

    @Override
    public IHyriPlayerSettings getPlayerSettings(UUID uuid) {
        return this.plugin.getAPI().getPlayerManager().getPlayer(uuid).getSettings();
    }

    @Override
    public IHyriPlayerSettings createPlayerSettings() {
        return new HyriPlayerSettings(true, true, HyriPrivateMessagesLevel.ALL, HyriPlayersVisibilityLevel.ALL, true, true, true, HyriLanguage.EN);
    }

    @Override
    public void resetPlayerSettings(IHyriPlayer player) {
        player.setSettings(this.createPlayerSettings());
    }

}
