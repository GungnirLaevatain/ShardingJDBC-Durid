package com.github.gungnirlaevatain.sharding.factory;

import com.github.gungnirlaevatain.sharding.ShardingConstants;
import com.github.gungnirlaevatain.sharding.adapter.ShardingKeyGeneratorAdapter;
import com.github.gungnirlaevatain.sharding.properties.ShardingKeyGeneratorProperty;
import com.github.gungnirlaevatain.sharding.rule.ShardingRule;
import com.github.gungnirlaevatain.sharding.rule.ShardingStrategy;
import com.github.gungnirlaevatain.sharding.rule.TableRule;
import com.github.gungnirlaevatain.sharding.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.*;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Slf4j
public class ShardingRuleConfigurationFactory {
    /**
     * New sharding rule configuration.
     * 生成分表分库规则配置
     *
     * @return the sharding rule configuration
     * @throws IllegalAccessException the illegal access exception
     * @throws InstantiationException the instantiation exception
     */
    public static ShardingRuleConfiguration newShardingRuleConfiguration(ShardingRule shardingRule) throws IllegalAccessException,
            InstantiationException {
        if (shardingRule != null) {
            ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
            shardingRuleConfiguration.setBindingTableGroups(shardingRule.getBindingTables());
            shardingRuleConfiguration.setBroadcastTables(shardingRule.getBroadcastTables());
            shardingRuleConfiguration.setDefaultDataSourceName(shardingRule.getDefaultDataSourceName());
            if (shardingRule.getDefaultKeyGenerator() != null) {
                shardingRuleConfiguration.setDefaultKeyGeneratorConfig(newKeyGeneratorConfiguration(shardingRule.getDefaultKeyGenerator()));
            }
            shardingRuleConfiguration.setDefaultDatabaseShardingStrategyConfig(newShardingStrategyConfiguration(shardingRule.getDefaultDatabaseStrategy()));
            shardingRuleConfiguration.setDefaultTableShardingStrategyConfig(newShardingStrategyConfiguration(shardingRule.getDefaultTableStrategy()));
            shardingRuleConfiguration.setTableRuleConfigs(newTableRuleConfiguration(shardingRule.getTableRules()));
            return shardingRuleConfiguration;
        }
        return null;
    }


    /**
     * New table rule configuration.
     * 生成分表配置
     *
     * @param tableRuleList the table rule list
     * @return the list
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     */
    private static List<TableRuleConfiguration> newTableRuleConfiguration(List<TableRule> tableRuleList) throws InstantiationException, IllegalAccessException {
        if (CollectionUtils.isEmpty(tableRuleList)) {
            return Collections.emptyList();
        }

        List<TableRuleConfiguration> resultList = new ArrayList<>(tableRuleList.size());
        for (TableRule rule : tableRuleList) {
            TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration(rule.getLogicTable(),
                    rule.getActualDataNodes());
            tableRuleConfiguration.setKeyGeneratorConfig(newKeyGeneratorConfiguration(rule.getKeyGenerator()));
            tableRuleConfiguration.setDatabaseShardingStrategyConfig(newShardingStrategyConfiguration(rule.getDatabaseStrategy()));
            tableRuleConfiguration.setTableShardingStrategyConfig(newShardingStrategyConfiguration(rule.getTableStrategy()));
            resultList.add(tableRuleConfiguration);
        }
        return resultList;
    }

    /**
     * New key generator configuration.
     * 根据配置生成主键生成策略
     *
     * @param property the property
     * @return the key generator configuration
     */
    private static KeyGeneratorConfiguration newKeyGeneratorConfiguration(ShardingKeyGeneratorProperty property) {
        if (property == null) {
            return null;
        }
        Properties classProperties = new Properties();
        classProperties.put(ShardingConstants.PROPERTY_CLASS_KEY, property.getKeyGenerator().getName());

        return new KeyGeneratorConfiguration(ShardingKeyGeneratorAdapter.class.getName(), property.getColumn(),
                classProperties);
    }

    /**
     * New sharding strategy configuration.
     * 根据配置获取分表算法实现类
     *
     * @param shardingStrategy the sharding strategy
     * @return the sharding strategy configuration
     * @throws IllegalAccessException the illegal access exception
     * @throws InstantiationException the instantiation exception
     */
    private static ShardingStrategyConfiguration newShardingStrategyConfiguration(ShardingStrategy shardingStrategy) throws IllegalAccessException, InstantiationException {
        if (shardingStrategy == null) {
            return null;
        }
        switch (shardingStrategy.getType()) {
            case HINT:
                return new HintShardingStrategyConfiguration(newOrGetBeanFromSpring(shardingStrategy.getHintAlgorithm()));
            case INLINE:
                return new InlineShardingStrategyConfiguration(shardingStrategy.getColumns(),
                        shardingStrategy.getInlineExpression());
            case COMPLEX:
                return new ComplexShardingStrategyConfiguration(shardingStrategy.getColumns(),
                        newOrGetBeanFromSpring(shardingStrategy.getComplexAlgorithm()));
            case STANDARD:
                return new StandardShardingStrategyConfiguration(shardingStrategy.getColumns(),
                        newOrGetBeanFromSpring(shardingStrategy.getPreciseAlgorithm()), shardingStrategy.getRangeAlgorithm() == null ? null : newOrGetBeanFromSpring(shardingStrategy.getRangeAlgorithm()));
            default: {
                return new NoneShardingStrategyConfiguration();
            }
        }
    }

    /**
     * New or get bean from spring.
     * 新建或从spring上下文获取对应的实现类
     *
     * @param <T> the type parameter
     * @param cls the cls
     * @return the t
     * @throws IllegalAccessException the illegal access exception
     * @throws InstantiationException the instantiation exception
     */
    private static <T> T newOrGetBeanFromSpring(Class<T> cls) throws IllegalAccessException, InstantiationException {
        try {
            return SpringBeanUtil.loadBeanByClass(cls);
        } catch (NoSuchBeanDefinitionException e) {
            log.info("can not get target class [{}] from application, because no such bean", cls);
        } catch (Exception e) {
            log.warn("get target class [{}] from application fail", cls, e);
        }

        log.info("create new target class [{}] from newInstance", cls);
        return cls.newInstance();
    }

}
