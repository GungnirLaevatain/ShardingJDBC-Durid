

package com.github.gungnirlaevatain.sharding.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class SpringBeanUtil {

    private static ApplicationContext applicationContext;

    public static <T> T loadBeanByClass(Class<T> cls) {
        return applicationContext.getBean(cls);
    }

    public static <T> T loadBeanByNameAndClass(String name, Class<T> cls) {
        return applicationContext.getBean(name, cls);
    }

    public static void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
}
