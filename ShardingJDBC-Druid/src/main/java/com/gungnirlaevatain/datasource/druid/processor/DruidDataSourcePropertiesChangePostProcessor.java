
package com.gungnirlaevatain.datasource.druid.processor;

import com.alibaba.druid.pool.DruidDataSource;
import com.gungnirlaevatain.datasource.druid.properties.DruidDataSourceProperty;
import com.gungnirlaevatain.datasource.druid.util.ReflectAnnotationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class DruidDataSourcePropertiesChangePostProcessor extends ConfigurationPropertiesBindingPostProcessor {
    private final DruidDataSourceProperty property;

    public DruidDataSourcePropertiesChangePostProcessor(DruidDataSourceProperty druidDataSourceProperty) {
        property = druidDataSourceProperty;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (property.getDbs().containsKey(beanName) && bean instanceof DruidDataSource) {
            ConfigurationProperties annotation = bean.getClass()
                    .getAnnotation(ConfigurationProperties.class);
            if (annotation == null) {
                return bean;
            }
            Map<String, Object> annotationMemberValues = ReflectAnnotationUtil
                    .getAnnotationMemberValues(annotation);
            String prefix = "prefix";
            String value = "value";
            String configName = camelToLine(beanName);
            annotationMemberValues.put(prefix, DruidDataSourceProperty.DB_PREFIX + "." + configName);
            annotationMemberValues.put(value, DruidDataSourceProperty.DB_PREFIX + "." + configName);
            bindProperty(bean, beanName, annotation);
        }
        return bean;
    }

    private void bindProperty(Object bean, String beanName, ConfigurationProperties annotation) {
        try {
            Method method = ConfigurationPropertiesBindingPostProcessor.class
                    .getDeclaredMethod("bind", Object.class, String.class, ConfigurationProperties.class);
            method.setAccessible(true);
            method.invoke(this, bean, beanName, annotation);
            log.info("bind config to data source named [{}] success", beanName);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("can not invoke method [bind] from ConfigurationPropertiesBindingPostProcessor", e);
        }
    }

    /**
     * Camel to underline.
     * 驼峰转为横线命名
     *
     * @param param the param
     * @return the string
     */
    private String camelToLine(String param) {
        if (StringUtils.isEmpty(param)) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("-");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }
}
