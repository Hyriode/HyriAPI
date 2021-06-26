package fr.hyriode.hyriapi.util;

import java.lang.reflect.Field;

public class Reflection {

    public static void setField(Field field, Object fieldObject, Object value) {
        try {
            field.setAccessible(true);
            field.set(fieldObject, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
