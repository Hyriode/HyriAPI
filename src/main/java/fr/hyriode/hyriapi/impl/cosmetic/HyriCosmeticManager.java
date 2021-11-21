package fr.hyriode.hyriapi.impl.cosmetic;

import fr.hyriode.hyriapi.cosmetic.HyriCosmetic;
import fr.hyriode.hyriapi.cosmetic.IHyriCosmeticManager;
import fr.hyriode.hyriapi.impl.HyriAPIPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by Yggdrasil80
 * on 23/07/2021 at 11:29
 */
public class HyriCosmeticManager implements IHyriCosmeticManager {

    private final HyriAPIPlugin plugin;
    private final List<Class<? extends HyriCosmetic>> cosmetics;

    public HyriCosmeticManager(HyriAPIPlugin plugin) {
        this.plugin = plugin;
        this.cosmetics = new ArrayList<>();
    }

    @Override
    public HyriCosmetic getCosmetic(String name) {
        try {
            return this.cosmetics.stream().filter(cos -> cos.getName().equalsIgnoreCase(name)).findFirst().orElse(null).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public HyriCosmetic getCosmetic(Class<? extends HyriCosmetic> cosmetic) {
        try {
            return this.cosmetics.stream().filter(cos -> cos.equals(cosmetic)).findFirst().orElse(null).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void registerCosmetic(Class<? extends HyriCosmetic> cosmetic) {
        this.cosmetics.add(cosmetic);
    }

    @Override
    public List<Class<? extends HyriCosmetic>> getRegisteredCosmetics() {
        return this.cosmetics;
    }

    @Override
    public List<Class<? extends HyriCosmetic>> getCosmetics(UUID uuid) {
        return this.plugin.getAPI().getPlayerManager().getPlayer(uuid).getCosmetics();
    }

    @Override
    public boolean hasCosmetic(Class<? extends HyriCosmetic> cosmetic, UUID uuid) {
        return this.plugin.getAPI().getPlayerManager().getPlayer(uuid).getCosmetics().contains(cosmetic);
    }

    @Override
    public void addCosmetic(Class<? extends HyriCosmetic> cosmetic, UUID uuid) {
        this.plugin.getAPI().getPlayerManager().getPlayer(uuid).addCosmetic(cosmetic);
    }

    @Override
    public void addCosmetics(List<Class<? extends HyriCosmetic>> list, UUID uuid) {
        for (Class<? extends HyriCosmetic> cosmetic : list) {
            this.plugin.getAPI().getPlayerManager().getPlayer(uuid).addCosmetic(cosmetic);
        }
    }

    @Override
    public void removeCosmetic(Class<? extends HyriCosmetic> cosmetic, UUID uuid) {
        this.plugin.getAPI().getPlayerManager().getPlayer(uuid).removeCosmetic(cosmetic);
    }

    @Override
    public void removeCosmetics(List<Class<? extends HyriCosmetic>> list, UUID uuid) {
        for (Class<? extends HyriCosmetic> cosmetic : list) {
            this.plugin.getAPI().getPlayerManager().getPlayer(uuid).removeCosmetic(cosmetic);
        }
    }

}
