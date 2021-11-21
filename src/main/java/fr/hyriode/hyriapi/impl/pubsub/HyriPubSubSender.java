package fr.hyriode.hyriapi.impl.pubsub;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.impl.HyriAPIPlugin;
import redis.clients.jedis.Jedis;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 23/07/2021 at 11:29
 */
class HyriPubSubSender implements Runnable {

    private final LinkedBlockingQueue<HyriPubSubMessage> messages;

    private boolean running = true;

    private Jedis jedis;

    public HyriPubSubSender() {
        this.messages = new LinkedBlockingQueue<>();
    }

    public void send(HyriPubSubMessage message) {
        this.messages.add(message);
    }

    @Override
    public void run() {
        this.check();

        while (this.running) {
            try {
                this.process(this.messages.take());
            } catch (InterruptedException e) {
                this.jedis.close();
                return;
            }
        }
    }

    private void process(HyriPubSubMessage message) {
        boolean published = false;

        while (!published) {
            try {
                final Runnable callback = message.getCallback();

                this.jedis.publish(message.getChannel(), message.getMessage());

                if (callback != null) {
                    callback.run();
                }

                published = true;
            } catch (Exception e) {
                this.check();
            }
        }
    }

    private void check() {
        try {
            this.jedis = HyriAPI.get().getRedisResource();
        } catch (Exception e) {
            HyriAPIPlugin.log(Level.SEVERE, "[" + this.getClass().getSimpleName() + "] Couldn't connect to Redis server. Error: " + e.getMessage() + ". Recheck in 5 seconds.");

            try {
                Thread.sleep(5000);

                this.check();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
