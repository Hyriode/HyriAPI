package fr.hyriode.api.application;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 10:56
 */
public interface IHyriApplication<S> {

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
     * @return The current state
     */
    S getState();

    /**
     * Set the current state of the application
     *
     * @param state The new state
     */
    void setState(S state);

}
