package fr.hyriode.api.impl.common.booster;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.HyriBoosterEvent;
import fr.hyriode.api.booster.HyriBoosterTransaction;
import fr.hyriode.api.booster.IHyriBooster;
import fr.hyriode.api.booster.IHyriBoosterManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.transaction.IHyriTransaction;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/04/2022 at 16:30
 */
public class HyriBoosterManager implements IHyriBoosterManager {

    private static final String KEY = "boosters:";
    private static final BiFunction<String, UUID, String> KEY_FORMATTER = (type, uuid) -> type + KEY + uuid.toString();

    @Override
    public IHyriBooster enableBooster(UUID owner, String type, double multiplier, long duration) {
        final IHyriBooster booster = new HyriBooster(type, multiplier, owner, duration);

        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final String key = KEY_FORMATTER.apply(booster.getType(), booster.getIdentifier());

            jedis.set(key, HyriAPI.GSON.toJson(booster));
            jedis.expire(key, duration);
        });

        HyriAPI.get().getNetworkManager().getEventBus().publish(new HyriBoosterEvent(booster));

        return booster;
    }

    @Override
    public void giveBooster(IHyriPlayer player, String type, double multiplier, long duration) {
        player.addTransaction(HyriBoosterTransaction.TRANSACTIONS_TYPE, new HyriBoosterTransaction(type, multiplier, duration));
        player.update();
    }

    @Override
    public Map<String, HyriBoosterTransaction> getPlayerBoosters(IHyriPlayer player) {
        final Map<String, HyriBoosterTransaction> boosters = new HashMap<>();
        final List<? extends IHyriTransaction> transactions = player.getTransactions(HyriBoosterTransaction.TRANSACTIONS_TYPE);

        if (transactions == null) {
            return boosters;
        }

        for (IHyriTransaction transaction : transactions) {
            boosters.put(transaction.name(), transaction.content(HyriBoosterTransaction.class));
        }
        return boosters;
    }

    @Override
    public List<IHyriBooster> getBoosters() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<IHyriBooster> boosters = new ArrayList<>();

            for (String key : jedis.keys(KEY + "*")) {
                boosters.add(HyriAPI.GSON.fromJson(jedis.get(key), HyriBooster.class));
            }
            return boosters;
        });
    }

    @Override
    public List<IHyriBooster> getBoosters(String type) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<IHyriBooster> boosters = new ArrayList<>();

            for (String key : jedis.keys(KEY + type + ":*")) {
                boosters.add(HyriAPI.GSON.fromJson(jedis.get(key), HyriBooster.class));
            }
            return boosters;
        });
    }

    @Override
    public IHyriBooster getBooster(UUID identifier) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final Set<String> keys = jedis.keys(KEY + "*:" + identifier.toString());

            if (keys != null && keys.size() > 0) {
                final String key = new ArrayList<>(keys).get(0);

                return HyriAPI.GSON.fromJson(jedis.get(key), HyriBooster.class);
            }
            return null;
        });
    }

}
