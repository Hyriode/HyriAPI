package fr.hyriode.api.http;

import org.apache.http.client.HttpClient;
import org.jetbrains.annotations.NotNull;

/**
 * Created by AstFaster
 * on 23/02/2023 at 10:44.<br>
 *
 * The requester used to perform HTTP requests.
 */
public interface IHttpRequester {

    /**
     * Get the HTTP client instance
     *
     * @return The {@link HttpClient} instance
     */
    @NotNull HttpClient getClient();

}
