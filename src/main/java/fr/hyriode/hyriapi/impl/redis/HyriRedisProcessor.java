package fr.hyriode.hyriapi.impl.redis;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.HyriAPIPlugin;
import fr.hyriode.hyriapi.redis.IHyriRedisProcessor;
import redis.clients.jedis.Jedis;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/11/2021 at 17:40
 */
public class HyriRedisProcessor implements IHyriRedisProcessor, Runnable {

    private Jedis jedis;

    private boolean running;

    private Thread thread;

    private final LinkedBlockingQueue<HyriRedisProcessorAction> actions;

    public HyriRedisProcessor() {
        this.actions = new LinkedBlockingQueue<>();

        this.start();
    }

    private void start() {
        HyriAPIPlugin.log("Starting Redis processor...");

        this.running = true;

        this.thread = new Thread(this);
        this.thread.start();
    }

    public void stop() {
        HyriAPIPlugin.log("Stopping Redis processor...");

        this.running  = false;

        this.thread.interrupt();
    }

    @Override
    public void process(Consumer<Jedis> action) {
        this.process(action, null);
    }

    @Override
    public void process(Consumer<Jedis> action, Runnable callback) {
        this.actions.add(new HyriRedisProcessorAction(action, callback));
    }

    @Override
    public void run() {
        this.check();

        while (this.running) {
            try {
                final HyriRedisProcessorAction action = this.actions.take();

                boolean performed = false;

                while (!performed) {
                    final Runnable callback = action.getCallback();

                    try (Jedis jedis = HyriAPI.get().getRedisResource()) {
                        if (jedis != null) {
                            action.getAction().accept(jedis);

                            if (callback != null) {
                                callback.run();
                            }

                            performed = true;
                        }
                    }
                }
            } catch (InterruptedException e) {
                this.jedis.close();
                return;
            }
        }
    }

    private void check() {
        try {
            this.jedis = HyriAPI.get().getRedisResource();
        } catch (Exception e) {
            HyriAPIPlugin.log(Level.SEVERE, "[" + this.getClass().getSimpleName() + "] Couldn't contact Redis server. Error: " + e.getMessage() + ". Rechecking in 5 seconds...");

            try {
                Thread.sleep(5000);

                this.check();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
