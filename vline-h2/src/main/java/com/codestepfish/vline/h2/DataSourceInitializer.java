package com.codestepfish.vline.h2;

import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.codestepfish.vline.core.h2.H2Properties;
import lombok.extern.slf4j.Slf4j;
import org.anyline.data.datasource.DataSourceHolder;
import org.anyline.data.runtime.DataRuntime;
import org.anyline.metadata.type.DatabaseType;
import org.flywaydb.core.Flyway;
import org.springframework.util.Assert;

@Slf4j
public class DataSourceInitializer {

    // 动态注册数据源
    public static void initDataSource(H2Node node) throws Exception {
        H2Properties properties = node.getH2();

        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(properties.getJdbcUrl());
        ds.setDbType(DbType.h2);
        ds.setDriverClassName(properties.getDriverClassName());
        ds.setUsername(properties.getUsername());
        ds.setPassword(properties.getPassword());
        ds.setInitialSize(5);
        ds.setMaxActive(10);
        ds.setMinIdle(5);
        ds.setMaxWait(60000L);
        ds.setValidationQuery("select 1");
        ds.setValidationQueryTimeout(2000);
        ds.setTestOnBorrow(false);
        ds.setTestOnReturn(false);
        ds.setTestWhileIdle(true);
        ds.setTimeBetweenEvictionRunsMillis(60000L);
        ds.setMinEvictableIdleTimeMillis(300000L);

        // retry
        ds.setBreakAfterAcquireFailure(false);
        ds.setTimeBetweenConnectErrorMillis(5000L);
        ds.setConnectionErrorRetryAttempts(Integer.MAX_VALUE);

        DataRuntime dataRuntime = DataSourceHolder.reg(node.getName(), ds, DatabaseType.H2);

        Assert.notNull(dataRuntime, String.format("【%s】 H2 DataSource Init Failed", node.getName()));

        log.info("【DataSource - H2】reg success: {}", node.getName());

        // flyway
        if (properties.getFlyway()) {
            Flyway.configure().dataSource(ds)
                    .baselineOnMigrate(true)
                    .locations(String.format("classpath:h2/%s", node.getName()))
                    .load().migrate();
        }
    }
}
