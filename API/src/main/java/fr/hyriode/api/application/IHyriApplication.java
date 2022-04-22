package fr.hyriode.api.application;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 10:56
 */
public interface IHyriApplication<T extends IHyriApplication.IState> {

    /**
     * Get the name of the application
     *
     * @return A name
     */
    String getName();

    /**
     * Get the time when the application started.<br>
     * The time is a timestamp in milliseconds
     *
     * @return A timestamp
     */
    long getStartedTime();

    /**
     * Get the current state of the application
     *
     * @return A {@link IState}
     */
    T getState();

    /**
     * Set the current state of the application
     *
     * @param state A new {@link IState}
     */
    void setState(T state);

    /**
     * Get the handler that will be triggered when the server will be stopped
     *
     * @return A {@link Runnable} to execute
     */
    Runnable getStopHandler();

    /**
     * Set the handler that will be triggered when the server will be stopped
     *
     * @param stopHandler The new {@link Runnable} to execute
     */
    void setStopHandler(Runnable stopHandler);

    /**
     * The interface that represents a state of the application
     */
    interface IState {}

}
