
package com.github.gungnirlaevatain.sharding.properties;

import lombok.Data;
import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;

@Data
public class ShardingKeyGeneratorProperty {

    /**
     * The Key generator column name.
     * 主键列名
     */
    private String column;
    /**
     * The Key generator.
     * 主键生成器
     */
    private Class<? extends ShardingKeyGenerator> keyGenerator;
}
