package fr.hyriode.api.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
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

    private final Map<Class<? extends HyriEvent>, Queue<Object>> eventHandlers = new ConcurrentHashMap<>();

    private final String name;

    public HyriEventBus(String name) {
        this.name = name;
    }

    @Override
    public void register(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (this.isMethodValid(method)) {
                final Class<? extends HyriEvent> eventClass = method.getParameterTypes()[0].asSubclass(HyriEvent.class);
                Queue<Object> objects = this.eventHandlers.get(eventClass);

                if (objects == null) {
                    objects = new ConcurrentLinkedQueue<>();
                }

                objects.add(object);

                this.eventHandlers.put(eventClass, objects);
            }
        }
    }

    @Override
    public void unregister(Object object) {
        for (Map.Entry<Class<? extends HyriEvent>, Queue<Object>> entry : this.eventHandlers.entrySet()) {
            final Queue<Object> objects = entry.getValue();

            objects.remove(object);

            this.eventHandlers.put(entry.getKey(), objects);
        }
    }

    @Override
    public void publish(HyriEvent event) {
        try {
            final Queue<Object> objects = this.eventHandlers.get(event.getClass());

            if (objects != null) {
                final Map<HyriEventPriority, Map.Entry<Object, Method>> methods = new HashMap<>();

                for (Object object : objects) {
                    final Class<?> objectClass = object.getClass();

                    for (Method method : objectClass.getDeclaredMethods()) {
                        if (this.isMethodValid(method)) {
                            methods.put(method.getAnnotation(HyriEventHandler.class).priority(), new AbstractMap.SimpleEntry<>(object, method));
                        }
                    }
                }

                for (Map.Entry<HyriEventPriority, Map.Entry<Object, Method>> sorted : methods.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList())) {
                    final Map.Entry<Object, Method> entry = sorted.getValue();

                    entry.getValue().invoke(entry.getKey(), event);
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
