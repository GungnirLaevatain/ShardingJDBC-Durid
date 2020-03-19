
package com.github.gungnirlaevatain.sharding;

import com.github.gungnirlaevatain.sharding.adapter.MasterSlaveLoadBalanceAlgorithmAdapter;
import com.github.gungnirlaevatain.sharding.adapter.ShardingKeyGeneratorAdapter;
import com.github.gungnirlaevatain.sharding.factory.MasterSlaveRuleConfigurationFactory;
import com.github.gungnirlaevatain.sharding.factory.OrchestrationConfigurationFactory;
import com.github.gungnirlaevatain.sharding.factory.ShardingRuleConfigurationFactory;
import com.github.gungnirlaevatain.sharding.properties.ShardingDataSourceProperty;
import com.github.gungnirlaevatain.sharding.rule.OrchestrationRule;
import com.github.gungnirlaevatain.sharding.util.ShardingSpiUtil;
import com.github.gungnirlaevatain.sharding.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.orchestration.config.OrchestrationConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.orchestration.api.OrchestrationMasterSlaveDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.orchestration.api.OrchestrationShardingDataSourceFactory;
import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;
import org.apache.shardingsphere.spi.masterslave.MasterSlaveLoadBalanceAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.gungnirlaevatain.sharding.ShardingConstants.DATA_SOURCE_NAME;

@Slf4j
@Configuration
@ComponentScan(basePackages = "com.gungnirlaevatain.sharding")
@EnableConfigurationProperties(ShardingDataSourceProperty.class)
public class ShardingDataSourceAutoConfiguration {
    @Autowired
    private ShardingDataSourceProperty property;
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        SpringBeanUtil.setApplicationContext(applicationContext);
        ShardingSpiUtil.register(MasterSlaveLoadBalanceAlgorithm.class, MasterSlaveLoadBalanceAlgorithmAdapter.class);
        ShardingSpiUtil.register(ShardingKeyGenerator.class, ShardingKeyGeneratorAdapter.class);
    }


    /**
     * create sharding data source.
     * 构建基于sharding-jdbc的数据源
     *
     * @return the sharding data source
     * 生成的数据源
     * @throws SQLException           the sql exception
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     */
    @Primary
    @Bean(name = DATA_SOURCE_NAME)
    public DataSource createShardingDataSource() throws SQLException, InstantiationException, IllegalAccessException {
        OrchestrationConfiguration orchestrationConfiguration = OrchestrationConfigurationFactory
                .newOrchestrationConfiguration(property.getOrchestrationRule());
        ShardingRuleConfiguration shardingRuleConfiguration = ShardingRuleConfigurationFactory
                .newShardingRuleConfiguration(property.getShardingRule());
        List<MasterSlaveRuleConfiguration> masterSlaveRuleConfigurations = MasterSlaveRuleConfigurationFactory
                .newMasterSlaveRuleConfiguration(property.getMasterSlaveRule());

        if (shardingRuleConfiguration != null && CollectionUtils.isNotEmpty(masterSlaveRuleConfigurations)) {
            shardingRuleConfiguration.setMasterSlaveRuleConfigs(masterSlaveRuleConfigurations);
        }

        if (orchestrationConfiguration == null) {
            if (shardingRuleConfiguration != null) {
                return ShardingDataSourceFactory.createDataSource(newDataSourceMap(), shardingRuleConfiguration,
                        property.getProp());
            }

            if (masterSlaveRuleConfigurations.size() != 1) {
                throw new IllegalStateException("MasterSlaveDataSourceFactory require only one masterSlaveRuleConfiguration");
            } else {
                return MasterSlaveDataSourceFactory.createDataSource(newDataSourceMap(),
                        masterSlaveRuleConfigurations.get(0), property.getProp());
            }
        } else {

            if (property.getOrchestrationRule().getType() == OrchestrationRule.Type.MASTER_SALVE) {
                if (masterSlaveRuleConfigurations.size() == 1) {
                    return OrchestrationMasterSlaveDataSourceFactory.createDataSource(newDataSourceMap(),
                            masterSlaveRuleConfigurations.get(0), property.getProp(), orchestrationConfiguration);
                } else {
                    return OrchestrationMasterSlaveDataSourceFactory.createDataSource(orchestrationConfiguration);
                }
            }
            if (shardingRuleConfiguration == null) {
                return OrchestrationShardingDataSourceFactory.createDataSource(orchestrationConfiguration);
            } else {
                return OrchestrationShardingDataSourceFactory.createDataSource(newDataSourceMap(),
                        shardingRuleConfiguration, property.getProp(), orchestrationConfiguration);
            }
        }


    }

    /**
     * New data source map.
     * 生成需要被管理的数据源配置
     *
     * @return the map
     */
    private Map<String, DataSource> newDataSourceMap() {
        Map<String, DataSource> dataSourceMap = new HashMap<>(property.getDataSources().size());
        property.getDataSources().forEach(dataSource -> dataSourceMap.put(dataSource,
                SpringBeanUtil.loadBeanByNameAndClass(dataSource, DataSource.class)));
        return dataSourceMap;
    }
}
