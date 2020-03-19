

package com.github.gungnirlaevatain.sharding.rule;

import com.github.gungnirlaevatain.sharding.properties.ShardingKeyGeneratorProperty;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShardingRule {

    /**
     * The Default data source name.
     * 默认数据源名称
     */
    private String defaultDataSourceName;
    /**
     * The Default key generator.
     * 默认id生成器
     */
    @NestedConfigurationProperty
    private ShardingKeyGeneratorProperty defaultKeyGenerator;
    /**
     * The Binding tables.
     * 需要被分表的表名列表
     */
    private List<String> bindingTables = new ArrayList<>();
    /**
     * The Broadcast tables.
     * 不进行路由,直接进行广播的表名列表
     */
    private List<String> broadcastTables = new ArrayList<>();
    /**
     * The Table rules.
     * 分表规则
     */
    private List<TableRule> tableRules;
    /**
     * The Default database strategy.
     * 默认的分库策略
     */
    @NestedConfigurationProperty
    private ShardingStrategy defaultDatabaseStrategy;
    /**
     * The Default table strategy.
     * 默认的分表策略
     */
    @NestedConfigurationProperty
    private ShardingStrategy defaultTableStrategy;
}
