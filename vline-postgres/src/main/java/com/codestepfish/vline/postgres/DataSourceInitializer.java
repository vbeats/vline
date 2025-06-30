package com.codestepfish.vline.postgres;

import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.codestepfish.vline.core.postgres.PostgresProperties;
import lombok.extern.slf4j.Slf4j;
import org.anyline.data.datasource.DataSourceHolder;
import org.anyline.data.runtime.DataRuntime;
import org.anyline.metadata.type.DatabaseType;
import org.flywaydb.core.Flyway;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Slf4j
public class DataSourceInitializer {

    // 动态注册数据源
    public static void initDataSource(PostgresNode node) throws Exception {
        PostgresProperties properties = node.getPostgres();

        if (!StringUtils.hasText(properties.getJdbcUrl())) {
            properties.setJdbcUrl(String.format("jdbc:postgresql://%s:%s/%s", properties.getHost(), properties.getPort(), properties.getDatabaseName()));
        }

        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(properties.getJdbcUrl());
        ds.setDbType(DbType.postgresql);
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

        DataRuntime dataRuntime = DataSourceHolder.reg(node.getName(), ds, DatabaseType.PostgreSQL);

        Assert.notNull(dataRuntime, String.format("数据源 :%s 初始化失败", node.getName()));

        log.info("【DataSource - PostgreSQL】reg success: {}", node.getName());

        // flyway
        Flyway.configure().dataSource(ds)
                .baselineOnMigrate(true)
                .locations(String.format("classpath:postgres/%s", node.getName()))
                .load().migrate();
    }
}
