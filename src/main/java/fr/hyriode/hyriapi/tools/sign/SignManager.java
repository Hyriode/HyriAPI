package fr.hyriode.hyriapi.tools.sign;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignManager {

    private final Map<UUID, Sign> signs;

    public SignManager(JavaPlugin plugin) {
        this.signs = new HashMap<>();

        new SignHandler(plugin, this);
    }

    protected void addGUI(UUID uuid, Sign sign) {
        this.signs.put(uuid, sign);
    }

    public Map<UUID, Sign> getSigns() {
        return this.signs;
    }
}
