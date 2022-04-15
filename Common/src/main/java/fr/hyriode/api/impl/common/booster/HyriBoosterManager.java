package fr.hyriode.api.impl.common.booster;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.IHyriBooster;
import fr.hyriode.api.booster.IHyriBoosterManager;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.hydrion.client.module.BoostersModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/04/2022 at 16:30
 */
public class HyriBoosterManager implements IHyriBoosterManager {

    private static final String KEY = "boosters:";
    private static final Function<UUID, String> KEY_FORMATTER = uuid -> KEY + uuid.toString();

    private final HydrionManager hydrionManager;
    private final BoostersModule boostersModule;

    public HyriBoosterManager(HydrionManager hydrionManager) {
        this.hydrionManager = hydrionManager;
        this.boostersModule = this.hydrionManager.getClient().getBoostersModule();
    }

    @Override
    public IHyriBooster enableBooster(String type, double amount, UUID purchaser, long purchaseDate, int duration) {
        final HyriBooster booster = new HyriBooster(type, amount, purchaser, purchaseDate);
        final long currentTime = System.currentTimeMillis();

        booster.setActivatedDate(currentTime);
        booster.setExpirationDate(currentTime + (duration * 1000L));

        this.addBooster(booster);

        return booster;
    }

    @Override
    public List<IHyriBooster> getActiveBoosters() {
        final Set<String> keys = HyriAPI.get().getRedisProcessor().get(jedis -> jedis.keys(KEY + "*"));

        if (keys != null && keys.size() > 0) {
            return HyriAPI.get().getRedisProcessor().get(jedis -> {
               final List<IHyriBooster> boosters = new ArrayList<>();

               for (String key : keys) {
                   final String json = jedis.get(key);

                   if (json != null) {
                       final IHyriBooster booster = HyriAPI.GSON.fromJson(json, HyriBooster.class);

                       if (booster.isActive()) {
                           boosters.add(booster);
                       } else {
                           this.removeBooster(booster);
                       }
                   }
               }
               return boosters;
            });
        }

        if (this.hydrionManager.isEnabled()) {
            try {
                return this.boostersModule.getBoosters().thenApply(response -> {
                    final JsonElement content = response.getContent();

                    if (!content.isJsonNull()) {
                        final List<IHyriBooster> boosters = HyriAPI.GSON.fromJson(content.getAsJsonArray(), new TypeToken<List<HyriBooster>>(){}.getType());

                        for (IHyriBooster booster : boosters) {
                            if (!booster.isActive()) {
                                this.removeBooster(booster);
                            }
                        }

                        return boosters;
                    }
                    return new ArrayList<IHyriBooster>();
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<IHyriBooster> getActiveBoosters(String type) {
        final List<IHyriBooster> all = this.getActiveBoosters();

        if (all == null) {
            return null;
        }

        final List<IHyriBooster> boosters = new ArrayList<>();

        for (IHyriBooster booster : all) {
            if (booster.getType().equals(type)) {
                boosters.add(booster);
            }
        }
        return boosters;
    }

    @Override
    public IHyriBooster getBooster(UUID identifier) {
        return null;
    }

    @Override
    public void addBooster(IHyriBooster booster) {
        final UUID identifier = booster.getIdentifier();
        final String json =  HyriAPI.GSON.toJson(booster);

        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(KEY_FORMATTER.apply(identifier), json));

        if (this.hydrionManager.isEnabled()) {
            this.boostersModule.addBooster(identifier, json);
        }
    }

    @Override
    public void removeBooster(UUID identifier) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(KEY_FORMATTER.apply(identifier)));

        if (this.hydrionManager.isEnabled()) {
            this.boostersModule.removeBooster(identifier);
        }
    }

}
