

package com.github.gungnirlaevatain.sharding.util;


import org.apache.shardingsphere.spi.NewInstanceServiceLoader;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ShardingSpiUtil {

    private static final String SPI_MAP_NAME = "SERVICE_MAP";
    private static final Map<Class, Collection<Class<?>>> SPI_MAP;

    static {
        Field field;
        try {
            field = NewInstanceServiceLoader.class.getDeclaredField(SPI_MAP_NAME);
            field.setAccessible(true);
            SPI_MAP = (Map<Class, Collection<Class<?>>>) field.get(NewInstanceServiceLoader.class);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void register(Class<?> cls, Class<?> instance) {
        Collection<Class<?>> instances = SPI_MAP.computeIfAbsent(cls, k -> new LinkedHashSet<>());
        instances.add(instance);
    }

}
