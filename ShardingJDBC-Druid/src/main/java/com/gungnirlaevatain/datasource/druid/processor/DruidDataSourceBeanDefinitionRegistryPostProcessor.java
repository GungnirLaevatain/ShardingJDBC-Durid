
package com.gungnirlaevatain.datasource.druid.processor;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.gungnirlaevatain.datasource.druid.properties.DruidDataSourceProperty;
import com.gungnirlaevatain.sharding.ShardingConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import javax.sql.DataSource;

@Slf4j
public class DruidDataSourceBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private final DruidDataSourceProperty property;

    public DruidDataSourceBeanDefinitionRegistryPostProcessor(DruidDataSourceProperty druidDataSourceProperty) {
        property = druidDataSourceProperty;
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        BeanDefinition shardingDataSource = registry
                .getBeanDefinition(ShardingConstants.DATA_SOURCE_NAME);
        shardingDataSource.setDependsOn(property.getDbs().keySet().toArray(new String[0]));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        property.getDbs().forEach((name, config) -> register(beanFactory, name));
    }

    private void register(ConfigurableListableBeanFactory beanFactory, String name) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(DataSource.class,
                        () -> DruidDataSourceBuilder.create().build());

        BeanDefinition personManagerBeanDefinition = beanDefinitionBuilder
                .getBeanDefinition();
        ((DefaultListableBeanFactory) beanFactory)
                .registerBeanDefinition(name, personManagerBeanDefinition);

    }
}
