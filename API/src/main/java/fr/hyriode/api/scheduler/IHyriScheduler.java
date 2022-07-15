package fr.hyriode.api.scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by AstFaster
 * on 14/07/2022 at 23:09
 */
public interface IHyriScheduler {

    /**
     * Schedule a task after a given delay and during a given period
     *
     * @param task The task to run
     * @param delay The delay before running the task
     * @param period The delay between each execution of the task
     * @param unit The unit for 'delay' and 'period' fields
     * @return The created {@link IHyriTask}
     */
    IHyriTask schedule(Runnable task, long delay, long period, TimeUnit unit);

    /**
     * Schedule a task after a given delay
     *
     * @param task The task to run
     * @param delay The delay before running the task
     * @param unit The unit for 'delay' field
     * @return The created {@link IHyriTask}
     */
    IHyriTask schedule(Runnable task, long delay, TimeUnit unit);

    /**
     * Run a simple task asynchronously
     *
     * @param task The task to run
     * @return The created {@link IHyriTask}
     */
    IHyriTask runAsync(Runnable task);

    /**
     * Cancel a task by giving its id
     *
     * @param taskId The id of the task to cancel
     */
    void cancel(int taskId);

    /**
     * Get the executor service instance of the scheduler
     *
     * @return The {@link ExecutorService} instance
     */
    ExecutorService getExecutorService();

}
