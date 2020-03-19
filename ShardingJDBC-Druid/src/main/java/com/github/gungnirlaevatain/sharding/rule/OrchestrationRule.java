
package com.github.gungnirlaevatain.sharding.rule;

import lombok.Data;
import org.apache.shardingsphere.orchestration.reg.api.RegistryCenterConfiguration;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
public class OrchestrationRule {

    /**
     * The Type.
     * 配置中心方式
     */
    private Type type;
    /**
     * The Name.
     * 名称
     */
    private String name;
    /**
     * The Overwrite.
     * 是否被本地配置覆盖
     */
    private boolean overwrite;
    /**
     * The Reg center config.
     */
    @NestedConfigurationProperty
    private RegistryCenterConfiguration regCenterConfig;

    public enum Type {
        /**
         * Sharding type.
         * 分片方式
         */
        SHARDING,
        /**
         * Master salve type.
         * 主从方式
         */
        MASTER_SALVE
    }
}
