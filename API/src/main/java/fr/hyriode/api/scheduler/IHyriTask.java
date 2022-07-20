package fr.hyriode.api.scheduler;

/**
 * Created by AstFaster
 * on 14/07/2022 at 23:09
 */
public interface IHyriTask extends Runnable {

    /**
     * Cancel the task
     */
    void cancel();

    /**
     * Add a runnable to run after the task execution process
     *
     * @param then The runnable to run after
     * @return This {@link IHyriTask} object
     */
    IHyriTask andThen(Runnable then);

    /**
     * Get the id of the task
     *
     * @return An id
     */
    int getId();

    /**
     * Check whether the task is running or not
     *
     * @return <code>true</code> if the task is running
     */
    boolean isRunning();


}
