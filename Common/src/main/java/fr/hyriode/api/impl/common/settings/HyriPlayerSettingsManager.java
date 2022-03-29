package fr.hyriode.api.impl.common.settings;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.settings.*;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
public class HyriPlayerSettingsManager implements IHyriPlayerSettingsManager {

    @Override
    public IHyriPlayerSettings getPlayerSettings(UUID uuid) {
        return HyriAPI.get().getPlayerManager().getPlayer(uuid).getSettings();
    }

    @Override
    public IHyriPlayerSettings createPlayerSettings() {
        return new HyriPlayerSettings(true, true, HyriPrivateMessagesLevel.ALL, HyriPlayersVisibilityLevel.ALL, true, true, true, HyriLanguage.EN, "global");
    }

    @Override
    public void resetPlayerSettings(IHyriPlayer player) {
        player.setSettings(this.createPlayerSettings());
    }

}
