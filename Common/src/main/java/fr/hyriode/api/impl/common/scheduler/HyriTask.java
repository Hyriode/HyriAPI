package fr.hyriode.api.impl.common.scheduler;

import fr.hyriode.api.scheduler.IHyriTask;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by AstFaster
 * on 14/07/2022 at 23:26
 */
public class HyriTask implements IHyriTask {

    private Runnable then;

    private final AtomicBoolean running = new AtomicBoolean(true);

    private final HyriScheduler scheduler;
    private final int id;
    private final Runnable task;
    private final long delay;
    private final long period;

    public HyriTask(HyriScheduler scheduler, int id, Runnable task, long delay, long period, TimeUnit unit) {
        this.scheduler = scheduler;
        this.id = id;
        this.task = task;
        this.delay = unit.toMillis(delay);
        this.period = unit.toMillis(period);
    }

    @Override
    public void run() {
        if (this.delay > 0) {
            try {
                Thread.sleep(this.delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        while (this.running.get()) {
            try {
                this.task.run();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (this.period <= 0) {
                break;
            }

            try {
                Thread.sleep(this.period);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        this.cancel();
    }

    @Override
    public IHyriTask andThen(Runnable then) {
        this.then = then;
        return this;
    }

    @Override
    public void cancel() {
        this.scheduler.cancel0(this);

        this.running.set(false);

        if (this.then != null) {
            this.then.run();
        }
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }

}
