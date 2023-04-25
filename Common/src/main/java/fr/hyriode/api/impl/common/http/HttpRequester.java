package fr.hyriode.api.impl.common.http;

import fr.hyriode.api.http.IHttpRequester;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by AstFaster
 * on 23/02/2023 at 10:51
 */
public class HttpRequester implements IHttpRequester {

    private final CloseableHttpClient client;

    public HttpRequester() {
        this.client = HttpClientBuilder.create()
                .setUserAgent("Hyriode HyriAPI/1.0")
                .build();
    }

    public void stop() {
        try {
            this.client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull CloseableHttpClient getClient() {
        return this.client;
    }

}
