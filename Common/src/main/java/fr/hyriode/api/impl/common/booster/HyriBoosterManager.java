package fr.hyriode.api.impl.common.booster;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.HyriBoosterEvent;
import fr.hyriode.api.booster.IHyriBooster;
import fr.hyriode.api.booster.IHyriBoosterManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/04/2022 at 16:30
 */
public class HyriBoosterManager implements IHyriBoosterManager {

    private static final String KEY = "boosters:";
    private static final Function<UUID, String> KEY_FORMATTER = uuid -> KEY + uuid.toString();

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
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
           final List<IHyriBooster> boosters = new ArrayList<>();

           for (String key : jedis.keys(KEY + "*")) {
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

    @Override
    public List<IHyriBooster> getActiveBoosters(String type) {
        final List<IHyriBooster> boosters = new ArrayList<>();

        for (IHyriBooster booster : this.getActiveBoosters()) {
            if (booster.getType().equals(type)) {
                boosters.add(booster);
            }
        }
        return boosters;
    }

    @Override
    public IHyriBooster getBooster(UUID identifier) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> HyriAPI.GSON.fromJson(jedis.get(KEY_FORMATTER.apply(identifier)), HyriBooster.class));
    }

    @Override
    public void addBooster(IHyriBooster booster) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(KEY_FORMATTER.apply(booster.getIdentifier()), HyriAPI.GSON.toJson(booster)));
        HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new HyriBoosterEvent(booster, HyriBoosterEvent.Action.ENABLED));
    }

    @Override
    public void removeBooster(UUID identifier) {
        final IHyriBooster booster = this.getBooster(identifier);

        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(KEY_FORMATTER.apply(identifier)));
        HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new HyriBoosterEvent(booster, HyriBoosterEvent.Action.REMOVED));
    }

}
