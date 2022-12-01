package fr.hyriode.api.util;

/**
 * Created by AstFaster
 * on 16/11/2022 at 15:25.<br>
 *
 * Exception thrown by HyriAPI processes.
 */
public class HyriAPIException extends RuntimeException {

    public HyriAPIException(String message) {
        super(message);
    }

    public HyriAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public HyriAPIException(Throwable cause) {
        super(cause);
    }

}
