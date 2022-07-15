package fr.hyriode.api.impl.common.scheduler;

import fr.hyriode.api.scheduler.IHyriScheduler;
import fr.hyriode.api.scheduler.IHyriTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by AstFaster
 * on 14/07/2022 at 23:26
 */
public class HyriScheduler implements IHyriScheduler {

    private final Lock writeLock;
    private final ExecutorService executorService;
    private final AtomicInteger tasksCounter;
    private final Map<Integer, IHyriTask> tasks;

    public HyriScheduler() {
        this.writeLock = new ReentrantReadWriteLock().writeLock();
        this.executorService = Executors.newCachedThreadPool();
        this.tasksCounter = new AtomicInteger();
        this.tasks = new HashMap<>();
    }

    public void stop() {
        this.executorService.shutdown();
    }

    @Override
    public IHyriTask schedule(Runnable runnable, long delay, long period, TimeUnit unit) {
        final IHyriTask task = new HyriTask(this, this.tasksCounter.getAndIncrement(), runnable, delay, period, unit);

        try {
            this.writeLock.lock();
            this.tasks.put(task.getId(), task);
        } finally {
            this.writeLock.unlock();
        }

        this.executorService.execute(runnable);

        return task;
    }

    @Override
    public IHyriTask schedule(Runnable task, long delay, TimeUnit unit) {
        return this.schedule(task, delay, 0L, unit);
    }

    @Override
    public IHyriTask runAsync(Runnable task) {
        return this.schedule(task, 0L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void cancel(int taskId) {
        IHyriTask task = this.tasks.get(taskId);

        if (task != null) {
            task.cancel();
        }
    }

    public void cancel0(IHyriTask task) {
        try {
            this.writeLock.lock();
            this.tasks.remove(task.getId());
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public ExecutorService getExecutorService() {
        return this.executorService;
    }

}
