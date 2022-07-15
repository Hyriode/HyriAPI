package fr.hyriode.api.mongodb.subscriber;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class OperationSubscriber<T> implements Subscriber<T> {

    private final List<T> received;
    private final List<Throwable> errors;
    private final CountDownLatch latch;

    private volatile Subscription subscription;

    public OperationSubscriber() {
        this.received = new ArrayList<>();
        this.errors = new ArrayList<>();
        this.latch =  new CountDownLatch(1);
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(Integer.MAX_VALUE);
    }

    @Override
    public void onNext(T received) {
        this.received.add(received);
    }

    @Override
    public void onError(Throwable error) {
        this.errors.add(error);
    }

    @Override
    public void onComplete() {
        this.latch.countDown();
    }

    public List<T> getAll(long timeout, TimeUnit unit) {
        try {
            this.subscription.request(Integer.MAX_VALUE);

            if (this.latch.await(timeout, unit)) {
                if (!this.errors.isEmpty()) {
                    throw this.errors.get(0);
                }

                return this.received;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> getAll() {
        return this.getAll(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    public T get(long timeout, TimeUnit unit) {
        final List<T> all = this.getAll(timeout, unit);

        return all == null || all.isEmpty() ? null : all.get(0);
    }

    public T get() {
        final List<T> all = this.getAll();

        return all == null || all.isEmpty() ? null : all.get(0);
    }

}

