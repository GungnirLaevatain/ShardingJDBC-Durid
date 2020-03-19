package com.github.gungnirlaevatain.sharding.factory;

import com.github.gungnirlaevatain.sharding.rule.OrchestrationRule;
import org.apache.shardingsphere.orchestration.config.OrchestrationConfiguration;

public class OrchestrationConfigurationFactory {
    /**
     * New orchestration configuration.
     * 生成配置中心的配置
     *
     * @return the orchestration configuration
     */
    public static OrchestrationConfiguration newOrchestrationConfiguration(OrchestrationRule orchestrationRule) {
        if (orchestrationRule != null) {
            return new OrchestrationConfiguration(orchestrationRule.getName(),
                    orchestrationRule.getRegCenterConfig(), orchestrationRule.isOverwrite());
        }
        return null;
    }
}
