
package com.github.gungnirlaevatain.sharding.rule;

import lombok.Data;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;

@Data
public class ShardingStrategy {

    /**
     * The Type.
     * 分片算法类型
     */
    private Type type;
    /**
     * The Columns.
     * 分片列名
     */
    private String columns;
    /**
     * The Inline expression.
     * 内联表达式
     */
    private String inlineExpression;
    /**
     * The Complex algorithm.
     * 多分片键时的分片策略
     */
    private Class<? extends ComplexKeysShardingAlgorithm> complexAlgorithm;
    /**
     * The Hint algorithm.
     * hint分片策略
     */
    private Class<? extends HintShardingAlgorithm> hintAlgorithm;
    /**
     * The Precise algorithm.
     * 单分片键时的分片策略
     */
    private Class<? extends PreciseShardingAlgorithm> preciseAlgorithm;
    /**
     * The Range algorithm.
     * 单分片键时的范围分片策略
     */
    private Class<? extends RangeShardingAlgorithm> rangeAlgorithm;


    public enum Type {
        /**
         * Standard type.
         * 单分片键策略
         */
        STANDARD,
        /**
         * Complex type.
         * 多分片键策略
         */
        COMPLEX,
        /**
         * Inline type.
         * 行表达式策略
         */
        INLINE,
        /**
         * Hint type.
         * Hint方式分片策略
         */
        HINT,
        /**
         * None type.
         * 不分片
         */
        NONE

    }
}
