package fr.hyriode.api.server.join;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 21:28
 */
public interface IHyriJoinManager {

    /**
     * Register a join handler instance
     *
     * @param priority The priority of the handler (lower priority will be called first)
     * @param handler The handler to register
     */
    void registerHandler(int priority, IHyriJoinHandler handler);

}
