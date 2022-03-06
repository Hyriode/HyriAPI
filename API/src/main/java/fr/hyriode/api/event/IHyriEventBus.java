package fr.hyriode.api.event;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 05/03/2022 at 17:58
 */
public interface IHyriEventBus {

    /**
     * Get the name of the event bus
     *
     * @return A name
     */
    String getName();

    /**
     * Register all the events in an object
     *
     * @param object The object with events to register
     */
    void register(Object object);

    /**
     * Unregister all the events in an object
     *
     * @param object The object with events to unregister
     */
    void unregister(Object object);

    /**
     * Publish an event on the eventbus
     *
     * @param event The {@link HyriEvent} to publish
     */
    void publish(HyriEvent event);

    /**
     * Publish an event on the eventbus but asynchronously
     *
     * @param event The {@link HyriEvent} to publish
     */
    void publishAsync(HyriEvent event);

}
