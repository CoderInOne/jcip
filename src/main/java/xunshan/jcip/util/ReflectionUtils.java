package xunshan.jcip.util;

import java.lang.reflect.Field;

public class ReflectionUtils {
    public static Object getFieldByName(Object o, String fn) {
        try {
            final Class<?> clazz = o.getClass();
            final Field field = clazz.getDeclaredField(fn);
            field.setAccessible(true);
            return field.get(o);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new ReflectionException(e.getMessage(), e);
        }
    }
}
