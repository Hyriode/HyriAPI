package fr.hyriode.api.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 05/03/2022 at 18:27
 */
public class HyriEventBus implements IHyriEventBus {

    protected final Map<Class<? extends HyriEvent>, Queue<HyriEventContext>> contexts = new ConcurrentHashMap<>();

    protected final String name;

    public HyriEventBus(String name) {
        this.name = name;
    }

    @Override
    public void register(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (this.isMethodValid(method)) {
                final Class<? extends HyriEvent> eventClass = method.getParameterTypes()[0].asSubclass(HyriEvent.class);

                Queue<HyriEventContext> contexts = this.contexts.get(eventClass);

                if (contexts == null) {
                    contexts = new ConcurrentLinkedQueue<>();
                }

                contexts.add(new HyriEventContext(method.getAnnotation(HyriEventHandler.class).priority(), object, method));

                this.contexts.put(eventClass, contexts);
            }
        }
    }

    @Override
    public void unregister(Object object) {
        for (Map.Entry<Class<? extends HyriEvent>, Queue<HyriEventContext>> entry : this.contexts.entrySet()) {
            final Queue<HyriEventContext> contexts = entry.getValue();

            contexts.removeIf(context -> context.getObject().equals(object));

            this.contexts.put(entry.getKey(), contexts);
        }
    }

    @Override
    public void publish(HyriEvent event) {
        try {
            final Queue<HyriEventContext> contexts = this.contexts.get(event.getClass());

            if (contexts != null) {
                for (HyriEventContext context : contexts.stream().sorted(Comparator.comparingInt(object -> object.getPriority().getWeight())).collect(Collectors.toList())) {
                    context.run(event);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publishAsync(HyriEvent event) {
        CompletableFuture.runAsync(() -> this.publish(event));
    }

    private boolean isMethodValid(Method method) {
        if (method.getModifiers() == Modifier.PUBLIC) {
            if (method.isAnnotationPresent(HyriEventHandler.class)) {
                if (method.getParameterCount() == 1) {
                    return HyriEvent.class.isAssignableFrom(method.getParameterTypes()[0]);
                }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
