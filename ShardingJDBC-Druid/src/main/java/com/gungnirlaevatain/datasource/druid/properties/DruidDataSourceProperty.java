
package com.gungnirlaevatain.datasource.druid.properties;


import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConditionalOnClass(DruidDataSource.class)
@ConfigurationProperties(DruidDataSourceProperty.PREFIX)
public class DruidDataSourceProperty {
    public static final String PREFIX = "spring.datasource.druid.complex";
    public static final String DB_PREFIX = "spring.datasource.druid.complex.dbs";
    private Map<String, Map<String, Object>> dbs = new HashMap<>();
}
