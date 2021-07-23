package fr.hyriode.hyriapi.impl.api.settings;

import fr.hyriode.hyriapi.impl.HyriPlugin;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.settings.HyriPlayersVisibilityLevel;
import fr.hyriode.hyriapi.settings.HyriPrivateMessagesLevel;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettingsManager;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:27
 */
public class HyriPlayerSettingsManager implements IHyriPlayerSettingsManager {

    private final HyriPlugin plugin;

    public HyriPlayerSettingsManager(HyriPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public IHyriPlayerSettings getPlayerSettings(UUID uuid) {
        return this.plugin.getAPI().getPlayerManager().getPlayer(uuid).getSettings();
    }

    @Override
    public IHyriPlayerSettings createPlayerSettings() {
        return new HyriPlayerSettings(true, true, HyriPrivateMessagesLevel.ALL, HyriPlayersVisibilityLevel.ALL, true, true, true);
    }

    @Override
    public void resetPlayerSettings(IHyriPlayer player) {
        player.setSettings(this.createPlayerSettings());
    }

}
