package fr.hyriode.api.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 25/04/2022 at 15:29
 */
public class HyriEventContext {

    private final HyriEventPriority priority;
    private final Object object;
    private final Method method;

    public HyriEventContext(HyriEventPriority priority, Object object, Method method) {
        this.priority = priority;
        this.object = object;
        this.method = method;
    }

    public HyriEventPriority getPriority() {
        return this.priority;
    }

    public Object getObject() {
        return this.object;
    }

    public Method getMethod() {
        return this.method;
    }

    public void run(HyriEvent event) throws InvocationTargetException, IllegalAccessException {
        this.method.invoke(this.object, event);
    }

}
