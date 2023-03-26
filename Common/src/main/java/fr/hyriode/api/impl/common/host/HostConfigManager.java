package fr.hyriode.api.impl.common.host;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.host.IHostConfig;
import fr.hyriode.api.host.IHostConfigManager;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

/**
 * Created by AstFaster
 * on 07/08/2022 at 13:42
 */
public class HostConfigManager implements IHostConfigManager {

    private static final char[] ID_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789".toCharArray();

    private static final String KEY = "host-configs:";

    private static final String LOADINGS_KEY = "host-configs-loadings";
    private static final String LIST_KEY = "host-configs-list";

    private static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(HostConfig.class, new HostConfig.Serializer())
            .create();

    @Override
    public IHostConfig createConfig(UUID owner, String game, String gameType, String name, String icon) {
        return new HostConfig(this.generateId(), owner, game, gameType, name, icon);
    }

    @Override
    public void saveConfig(IHostConfig config) {
        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final Pipeline pipeline = jedis.pipelined();
            final String configId = config.getId();

            pipeline.set(KEY + config.getOwner().toString() + ":" + configId, GSON.toJson(config));
            pipeline.rpush(LIST_KEY, configId);
            pipeline.sync();
        });
    }

    @Override
    public void deleteConfig(IHostConfig config) {
        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final Pipeline pipeline = jedis.pipelined();
            final String configId = config.getId();

            pipeline.del(KEY + config.getOwner().toString() + ":" + configId, GSON.toJson(config));
            pipeline.zrem(LOADINGS_KEY, configId);
            pipeline.lrem(LIST_KEY, 1, configId);
            pipeline.sync();
        });
    }

    @Override
    public IHostConfig getConfig(String id) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            for (String key : jedis.keys(KEY + "*:" + id)) {
                return GSON.fromJson(jedis.get(key), HostConfig.class);
            }
            return null;
        });
    }

    @Override
    public boolean existsConfig(String id) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.keys(KEY + "*:" + id).size() > 0);
    }

    @Override
    public long getConfigLoadings(String id) {
        try {
            return (long) (double) HyriAPI.get().getRedisProcessor().get(jedis -> jedis.zscore(LOADINGS_KEY, id));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public void addConfigLoading(String id) {
        if (!this.existsConfig(id)) {
            return;
        }

        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.zincrby(LOADINGS_KEY, 1.0D, id));
    }

    @Override
    public List<String> getPlayerConfigs(UUID playerId) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
           final List<String> configs = new ArrayList<>();
           final Set<String> keys = jedis.keys(KEY + playerId.toString() + ":*");

           for (String key : keys) {
               configs.add(key.split(":")[2]);
           }
           return configs;
        });
    }

    @Override
    public List<String> getConfigs(long start, long stop) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.lrange(LIST_KEY, start, stop));
    }

    private String generateId() {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            builder.append(ID_CHARS[ThreadLocalRandom.current().nextInt(ID_CHARS.length)]);
        }
        return builder.toString();
    }

}
