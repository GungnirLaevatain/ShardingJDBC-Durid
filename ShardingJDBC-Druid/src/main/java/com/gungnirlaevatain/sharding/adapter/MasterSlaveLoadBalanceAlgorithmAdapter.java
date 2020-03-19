
package com.gungnirlaevatain.sharding.adapter;

import com.gungnirlaevatain.sharding.ShardingConstants;
import com.gungnirlaevatain.sharding.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.spi.masterslave.MasterSlaveLoadBalanceAlgorithm;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Properties;

@Slf4j
public class MasterSlaveLoadBalanceAlgorithmAdapter implements MasterSlaveLoadBalanceAlgorithm {

    private MasterSlaveLoadBalanceAlgorithm algorithm;
    private Properties properties;

    @Override
    public String getDataSource(String name, String masterDataSourceName, List<String> slaveDataSourceNames) {
        return algorithm.getDataSource(name, masterDataSourceName, slaveDataSourceNames);
    }

    @Override
    public String getType() {
        return MasterSlaveLoadBalanceAlgorithmAdapter.class.getName();
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
            algorithm = (MasterSlaveLoadBalanceAlgorithm) SpringBeanUtil.loadBeanByClass(cls);
        } catch (NoSuchBeanDefinitionException e) {
            log.info("can not get target class [{}] from application, because no such bean", cls);
        } catch (Exception e) {
            log.warn("get target class [{}] from application fail", cls, e);
        }
        algorithm = (MasterSlaveLoadBalanceAlgorithm) cls.newInstance();
    }
}
