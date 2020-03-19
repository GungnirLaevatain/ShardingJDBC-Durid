
package com.gungnirlaevatain.sharding.rule;

import lombok.Data;
import org.apache.shardingsphere.spi.masterslave.MasterSlaveLoadBalanceAlgorithm;

import java.util.ArrayList;
import java.util.List;


@Data
public class MasterSlaveRule {

    /**
     * The Name.
     * 名称
     */
    private String name;
    /**
     * The Master data source name.
     * 主数据源名称
     */
    private String masterDataSourceName;
    /**
     * The Slave data source names.
     * 子数据源名称集合
     */
    private List<String> slaveDataSourceNames = new ArrayList<>();
    /**
     * The Load balance algorithm.
     * 路由策略
     */
    private Class<? extends MasterSlaveLoadBalanceAlgorithm> loadBalanceAlgorithm;

}
