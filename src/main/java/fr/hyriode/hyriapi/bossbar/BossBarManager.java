package fr.hyriode.hyriapi.bossbar;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BossBarManager {

    private static final Map<Player, BossBar> bossBars = new ConcurrentHashMap<>();

    public static BossBar getBar(Player player) {
        return bossBars.get(player);
    }

    public static BossBar setBar(JavaPlugin plugin, Player player, List<String> titles, int delay) {
        final BossBar bossBar = new BossBar(plugin, player, titles, delay);

        bossBar.setVisible(true);

        bossBars.put(player, bossBar);

        return bossBar;
    }

    public static BossBar setBar(JavaPlugin plugin, Player player, String title) {
        return setBar(plugin, player, Collections.singletonList(title), 0);
    }

    public static void removeBar(Player player) {
        bossBars.get(player).destroy();
        bossBars.remove(player);
    }

    public static boolean hasBar(Player player) {
        return bossBars.containsKey(player);
    }

    public static List<String> getBarTitles(Player player) {
        if (hasBar(player)) {
            return getBar(player).getTitles();
        }
        throw new NullPointerException();
    }

    public static void setBarTitles(Player player, List<String> titles) {
        if (hasBar(player)) {
            getBar(player).setTitles(titles);
        }
    }

    public static void setBarTitle(Player player, String title) {
        setBarTitles(player, Collections.singletonList(title));
    }

    public static float getBarProgress(Player player) {
        if (hasBar(player)) {
            return getBar(player).getProgress();
        }
        throw new NullPointerException();
    }

    public static void setBarProgress(Player player, float progress) {
        if (hasBar(player)) {
            getBar(player).setProgress(progress);
        }
    }

}
