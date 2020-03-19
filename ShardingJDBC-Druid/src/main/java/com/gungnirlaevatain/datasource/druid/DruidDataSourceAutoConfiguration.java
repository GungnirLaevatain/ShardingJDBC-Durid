
package com.gungnirlaevatain.datasource.druid;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.gungnirlaevatain.datasource.druid.processor.DruidDataSourceBeanDefinitionRegistryPostProcessor;
import com.gungnirlaevatain.datasource.druid.processor.DruidDataSourcePropertiesChangePostProcessor;
import com.gungnirlaevatain.datasource.druid.properties.DruidDataSourceProperty;
import com.gungnirlaevatain.sharding.ShardingDataSourceAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@AutoConfigureAfter(ShardingDataSourceAutoConfiguration.class)
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
@ConditionalOnClass(DruidDataSource.class)
@ComponentScan("com.gungnirlaevatain.datasource.druid")
public class DruidDataSourceAutoConfiguration {

    @Bean
    public static DruidDataSourceBeanDefinitionRegistryPostProcessor beanDefinitionRegistryPostProcessor(DruidDataSourceProperty property) {
        return new DruidDataSourceBeanDefinitionRegistryPostProcessor(property);
    }

    @Bean
    public static DruidDataSourcePropertiesChangePostProcessor beanPostProcessor(DruidDataSourceProperty property) {
        return new DruidDataSourcePropertiesChangePostProcessor(property);
    }

    @Bean
    public static DruidDataSourceProperty druidDataSourceProperty(@Autowired Environment environment) {
        Binder binder = Binder.get(environment);
        BindResult<DruidDataSourceProperty> result = binder.bind(DruidDataSourceProperty.PREFIX,
                DruidDataSourceProperty.class);
        if (result.isBound()) {
            return result.get();
        }
        return new DruidDataSourceProperty();
    }
}
