package fr.hyriode.api.mongodb.subscriber;

import java.util.function.Consumer;

public class CallbackSubscriber<T> extends OperationSubscriber<T> {

    private Consumer<T> onComplete;

    @Override
    public void onNext(T received) {
        super.onNext(received);

        if (this.onComplete != null) {
            this.onComplete.accept(received);
        }
    }

    public void whenComplete(Consumer<T> onComplete) {
        this.onComplete = onComplete;
    }

}
