
package com.gungnirlaevatain.sharding.properties;

import com.gungnirlaevatain.sharding.rule.MasterSlaveRule;
import com.gungnirlaevatain.sharding.rule.OrchestrationRule;
import com.gungnirlaevatain.sharding.rule.ShardingRule;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@Data
@ConfigurationProperties(ShardingDataSourceProperty.PREFIX)
public class ShardingDataSourceProperty {

    public static final String PREFIX = "sharding";
    /**
     * The Sharding rule.
     * 分表分库的规则
     */
    @NestedConfigurationProperty
    private ShardingRule shardingRule;
    /**
     * The Master slave rule.
     * 主从的规则
     */
    private List<MasterSlaveRule> masterSlaveRule = new ArrayList<>();
    /**
     * The Orchestration rule.
     * 配置中心规则
     */
    @NestedConfigurationProperty
    private OrchestrationRule orchestrationRule;
    /**
     * The Data sources.
     * 需要被管理的数据源名称
     */
    private List<String> dataSources = new ArrayList<>();
    /**
     * The Prop.
     * 额外配置
     */
    private Properties prop = new Properties();
}
