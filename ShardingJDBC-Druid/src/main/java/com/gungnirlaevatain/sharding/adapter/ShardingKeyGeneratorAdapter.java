
package com.gungnirlaevatain.sharding.adapter;

import com.gungnirlaevatain.sharding.ShardingConstants;
import com.gungnirlaevatain.sharding.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.StringUtils;

import java.util.Properties;

@Slf4j
public class ShardingKeyGeneratorAdapter implements ShardingKeyGenerator {

    private ShardingKeyGenerator shardingKeyGenerator;
    private Properties properties;

    @Override
    public Comparable<?> generateKey() {
        return shardingKeyGenerator.generateKey();
    }

    @Override
    public String getType() {
        return ShardingKeyGeneratorAdapter.class.getName();
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
        try {
            afterPropertySet();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            log.error("can not create ShardingKeyGenerator for properties : [{}] ", properties, e);
        }
    }

    private void afterPropertySet() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = properties.getProperty(ShardingConstants.PROPERTY_CLASS_KEY);
        if (StringUtils.isEmpty(className)) {
            return;
        }
        Class<?> cls = Class.forName(className);
        try {
            shardingKeyGenerator = (ShardingKeyGenerator) SpringBeanUtil.loadBeanByClass(cls);
        } catch (NoSuchBeanDefinitionException e) {
            log.info("can not get target class [{}] from application, because no such bean", cls);
        } catch (Exception e) {
            log.warn("get target class [{}] from application fail", cls, e);
        }
        shardingKeyGenerator = (ShardingKeyGenerator) cls.newInstance();
    }
}
