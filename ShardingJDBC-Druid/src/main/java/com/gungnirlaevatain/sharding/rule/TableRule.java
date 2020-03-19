
package com.gungnirlaevatain.sharding.rule;

import com.gungnirlaevatain.sharding.properties.ShardingKeyGeneratorProperty;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
public class TableRule {

    /**
     * The Logic table.
     * 原始表名
     */
    private String logicTable;
    /**
     * The Actual data nodes.
     * 当不存在分片键时，进行广播查询的表集合
     */
    private String actualDataNodes;
    /**
     * The Key generator.
     * 主键生成器
     */
    @NestedConfigurationProperty
    private ShardingKeyGeneratorProperty keyGenerator;
    /**
     * The Database strategy.
     * 分库策略
     */
    private ShardingStrategy databaseStrategy;
    /**
     * The Table strategy.
     * 分表策略
     */
    private ShardingStrategy tableStrategy;
}
