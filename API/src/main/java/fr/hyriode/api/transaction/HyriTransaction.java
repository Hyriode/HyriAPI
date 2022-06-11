package fr.hyriode.api.transaction;

/**
 * Created by AstFaster
 * on 11/06/2022 at 19:57
 */
public abstract class HyriTransaction {

    private final String name;
    private final long time;
    private final String message;

    public HyriTransaction(String name, long time, String message) {
        this.name = name;
        this.time = time;
        this.message = message;
    }

    public String getName() {
        return this.name;
    }

    public long getTime() {
        return this.time;
    }

    public String getMessage() {
        return this.message;
    }

}
