package com.gungnirlaevatain.sharding.factory;

import com.gungnirlaevatain.sharding.ShardingConstants;
import com.gungnirlaevatain.sharding.adapter.MasterSlaveLoadBalanceAlgorithmAdapter;
import com.gungnirlaevatain.sharding.rule.MasterSlaveRule;
import org.apache.shardingsphere.api.config.masterslave.LoadBalanceStrategyConfiguration;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.spi.masterslave.MasterSlaveLoadBalanceAlgorithm;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class MasterSlaveRuleConfigurationFactory {

    /**
     * New master slave rule configuration.
     * 生成主从配置
     *
     * @return the list
     */
    public static List<MasterSlaveRuleConfiguration> newMasterSlaveRuleConfiguration(Collection<MasterSlaveRule> masterSlaveRules) {

        if (CollectionUtils.isEmpty(masterSlaveRules)) {
            return Collections.emptyList();
        }
        List<MasterSlaveRuleConfiguration> resultList = new ArrayList<>(masterSlaveRules.size());
        for (MasterSlaveRule rule : masterSlaveRules) {
            MasterSlaveRuleConfiguration masterSlaveRuleConfiguration = newMasterSlaveRuleConfiguration(rule);
            resultList.add(masterSlaveRuleConfiguration);
        }
        return resultList;
    }

    private static MasterSlaveRuleConfiguration newMasterSlaveRuleConfiguration(MasterSlaveRule rule) {
        if (rule.getLoadBalanceAlgorithm() == null) {
            return new MasterSlaveRuleConfiguration(rule.getName(), rule.getMasterDataSourceName(),
                    rule.getSlaveDataSourceNames());
        }
        Class<? extends MasterSlaveLoadBalanceAlgorithm> cls = rule.getLoadBalanceAlgorithm();
        Properties classProperties = new Properties();
        classProperties.put(ShardingConstants.PROPERTY_CLASS_KEY, cls.getName());
        LoadBalanceStrategyConfiguration loadBalanceStrategyConfiguration =
                new LoadBalanceStrategyConfiguration(MasterSlaveLoadBalanceAlgorithmAdapter.class.getName(),
                        classProperties);

        return new MasterSlaveRuleConfiguration(rule.getName(), rule.getMasterDataSourceName(),
                rule.getSlaveDataSourceNames(), loadBalanceStrategyConfiguration);
    }

}
