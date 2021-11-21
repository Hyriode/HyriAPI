package fr.hyriode.hyriapi.impl.redis;

import redis.clients.jedis.Jedis;

import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/11/2021 at 17:53
 */
public class HyriRedisProcessorAction {

    private final Consumer<Jedis> action;
    private final Runnable callback;

    public HyriRedisProcessorAction(Consumer<Jedis> action, Runnable callback) {
        this.action = action;
        this.callback = callback;
    }

    public Consumer<Jedis> getAction() {
        return this.action;
    }

    public Runnable getCallback() {
        return this.callback;
    }

}
