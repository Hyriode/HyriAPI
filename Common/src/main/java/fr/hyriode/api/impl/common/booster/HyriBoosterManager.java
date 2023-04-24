package fr.hyriode.api.impl.common.booster;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.*;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.IHyriTransaction;
import fr.hyriode.api.rank.StaffRank;
import redis.clients.jedis.Pipeline;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static fr.hyriode.api.booster.IHyriBooster.*;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 14/04/2022 at 16:30
 */
public class HyriBoosterManager implements IHyriBoosterManager {

    private static final String KEY = "boosters:";
    private static final String THANKS_KEY = "boosters-thanks:";

    @Override
    public IHyriBooster enableBooster(UUID owner, String game, double multiplier, long duration) {
        final IHyriPlayer account = IHyriPlayer.get(owner);
        final Type type = account.getRank().is(StaffRank.ADMINISTRATOR) ? Type.COMBINED : Type.NORMAL;
        final List<IHyriBooster> boosters = this.getBoosters(game)
                .stream()
                .sorted(Comparator.comparingLong(IHyriBooster::getEnabledDate))
                .collect(Collectors.toList());
        final long enabledDate = boosters.size() == 0 || type == Type.COMBINED ? System.currentTimeMillis() : boosters.get(boosters.size() - 1).getDisabledDate();
        final HyriBooster booster = new HyriBooster(game, type, multiplier, owner, duration, enabledDate);

        HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
            final byte[] key = (KEY + game + ":" + booster.getIdentifier().toString()).getBytes(StandardCharsets.UTF_8);
            final Pipeline pipeline = jedis.pipelined();

            pipeline.set(key, HyriAPI.get().getDataSerializer().serialize(booster));
            pipeline.expire(key, (System.currentTimeMillis() - booster.getEnabledDate()) / 1000 + duration);
            pipeline.sync();

            HyriAPI.get().getNetworkManager().getEventBus().publish(new BoosterQueuedEvent(booster.getIdentifier()));
        });

        return booster;
    }

    @Override
    public void giveBooster(IHyriPlayer player, double multiplier, long duration) {
        player.getTransactions().add(HyriBoosterTransaction.TRANSACTIONS_TYPE, new HyriBoosterTransaction(multiplier, duration));
        player.update();
    }

    @Override
    public Map<String, HyriBoosterTransaction> getPlayerBoosters(IHyriPlayer player) {
        final Map<String, HyriBoosterTransaction> boosters = new HashMap<>();
        final List<IHyriTransaction> transactions = player.getTransactions().getAll(HyriBoosterTransaction.TRANSACTIONS_TYPE);

        if (transactions == null) {
            return boosters;
        }

        for (IHyriTransaction transaction : transactions) {
            boosters.put(transaction.name(), transaction.loadContent(new HyriBoosterTransaction()));
        }
        return boosters;
    }

    @Override
    public List<IHyriBooster> getBoosters() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<IHyriBooster> boosters = new ArrayList<>();

            for (byte[] key : jedis.keys((KEY + "*").getBytes(StandardCharsets.UTF_8))) {
                boosters.add(HyriAPI.get().getDataSerializer().deserialize(new HyriBooster(), jedis.get(key)));
            }
            return boosters;
        });
    }

    @Override
    public List<IHyriBooster> getBoosters(String game) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<IHyriBooster> boosters = new ArrayList<>();

            for (byte[] key : jedis.keys((KEY + game + ":*").getBytes(StandardCharsets.UTF_8))) {
                boosters.add(HyriAPI.get().getDataSerializer().deserialize(new HyriBooster(), jedis.get(key)));
            }
            return boosters;
        });
    }

    @Override
    public List<IHyriBooster> getActiveBoosters() {
        return this.getBoosters()
                .stream()
                .filter(IHyriBooster::isEnabled)
                .collect(Collectors.toList());
    }

    @Override
    public List<IHyriBooster> getActiveBoosters(String game) {
        return this.getBoosters(game)
                .stream()
                .filter(IHyriBooster::isEnabled)
                .collect(Collectors.toList());
    }

    @Override
    public IHyriBooster getBooster(UUID identifier) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final Set<String> keys = jedis.keys(KEY + "*:" + identifier.toString());

            if (keys != null && keys.size() > 0) {
                final String key = new ArrayList<>(keys).get(0);

                return HyriAPI.get().getDataSerializer().deserialize(new HyriBooster(), jedis.get(key.getBytes(StandardCharsets.UTF_8)));
            }
            return null;
        });
    }

    @Override
    public Set<UUID> getThanks(UUID booster) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final Set<UUID> result = new HashSet<>();
            final Set<String> thanks = jedis.smembers(THANKS_KEY + booster.toString());

            if (thanks == null) {
                return result;
            }

            for (String playerId : thanks) {
                result.add(UUID.fromString(playerId));
            }
            return result;
        });
    }

    @Override
    public void addThank(UUID booster, UUID player) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
            final boolean exists = jedis.exists(THANKS_KEY + booster.toString());

            jedis.sadd(THANKS_KEY + booster, player.toString());

            if (!exists) {
                jedis.expire(THANKS_KEY + booster, (this.getBooster(booster).getDisabledDate() - System.currentTimeMillis()) / 1000);
            }
        });
    }

    @Override
    public boolean hasThanked(UUID booster, UUID player) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.sismember(THANKS_KEY + booster.toString(), player.toString()));
    }

}
