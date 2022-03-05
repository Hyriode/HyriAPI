package fr.hyriode.api.impl.server.util;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/02/2022 at 15:31
 */
public class SpigotReflection {

    public static Object getHandle(Player player) {
        return invokeMethod(player, "getHandle");
    }

    public static Object invokeMethod(Object object, String methodName) {
        try {
            final Method method = object.getClass().getMethod(methodName);

            return method.invoke(object);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeField(Object object, String fieldName) {
        try {
            final Field field = object.getClass().getField(fieldName);

            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
