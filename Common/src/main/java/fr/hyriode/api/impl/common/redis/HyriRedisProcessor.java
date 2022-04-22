package fr.hyriode.api.impl.common.redis;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.HyriCommonImplementation;
import fr.hyriode.api.redis.IHyriRedisProcessor;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/11/2021 at 17:40
 */
public class HyriRedisProcessor implements IHyriRedisProcessor {

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public void stop() {
        HyriCommonImplementation.log("Stopping Redis processor (waiting for last requests to be done)...");

        try {
            if (!this.executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                HyriCommonImplementation.log("Redis processor couldn't handle last Redis requests!");

                this.executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            this.executorService.shutdownNow();
        }
    }

    @Override
    public void process(Consumer<Jedis> action) {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            if (jedis != null) {
                action.accept(jedis);
            }
        }
    }

    @Override
    public void processAsync(Consumer<Jedis> action) {
        this.executorService.execute(() -> this.process(action));
    }

    @Override
    public <R> R get(Function<Jedis, R> action) {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            if (jedis != null) {
                return action.apply(jedis);
            }
        }
        return null;
    }

    @Override
    public <R> CompletableFuture<R> getAsync(Function<Jedis, R> action) {
        return CompletableFuture.supplyAsync(() -> get(action), this.executorService);
    }

}
