package fr.hyriode.api.impl.common.cosmetic;

import fr.hyriode.api.cosmetic.HyriCosmetic;
import fr.hyriode.api.cosmetic.IHyriCosmeticManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by Yggdrasil80
 * on 23/07/2021 at 11:29
 */
public class HyriCosmeticManager implements IHyriCosmeticManager {

    private final List<Class<? extends HyriCosmetic>> cosmetics;

    public HyriCosmeticManager() {
        this.cosmetics = new ArrayList<>();
    }

    @Override
    public HyriCosmetic getCosmetic(String name) {
        try {
            final Class<? extends HyriCosmetic> clazz = this.cosmetics.stream().filter(cos -> cos.getName().equalsIgnoreCase(name)).findFirst().orElse(null);

            if (clazz != null) {
                return clazz.getConstructor().newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public HyriCosmetic getCosmetic(Class<? extends HyriCosmetic> cosmetic) {
        try {
            return this.cosmetics.stream().filter(cos -> cos.equals(cosmetic)).findFirst().orElse(null).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
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
        return new ArrayList<>();
    }

    @Override
    public boolean hasCosmetic(Class<? extends HyriCosmetic> cosmetic, UUID uuid) {
        return true;
    }

    @Override
    public void addCosmetic(Class<? extends HyriCosmetic> cosmetic, UUID uuid) {

    }

    @Override
    public void addCosmetics(List<Class<? extends HyriCosmetic>> list, UUID uuid) {

    }

    @Override
    public void removeCosmetic(Class<? extends HyriCosmetic> cosmetic, UUID uuid) {

    }

    @Override
    public void removeCosmetics(List<Class<? extends HyriCosmetic>> list, UUID uuid) {

    }

}
