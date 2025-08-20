package com.codestepfish.vline.mssql2008r2;

import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.codestepfish.vline.core.mssql.MssqlProperties;
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
    public static void initDataSource(MssqlNode node) throws Exception {
        MssqlProperties properties = node.getMssql();

        if (!StringUtils.hasText(properties.getJdbcUrl())) {
            properties.setJdbcUrl(String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=%s;trustServerCertificate=%s;", properties.getHost(), properties.getPort(), properties.getDatabaseName(), properties.getEncrypt(), properties.getTrustServerCertificate()));
        }

        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(properties.getJdbcUrl());
        ds.setDbType(DbType.sqlserver);
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

        DataRuntime dataRuntime = DataSourceHolder.reg(node.getName(), ds, DatabaseType.MSSQL);

        Assert.notNull(dataRuntime, String.format("【 %s 】 DataSource Init Failed", node.getName()));

        log.info("【DataSource - Sql Server】reg success: {}", node.getName());

        // flyway
        Flyway.configure().dataSource(ds)
                .baselineOnMigrate(true)
                .locations(String.format("classpath:sqlserver/%s", node.getName()))
                .load().migrate();
    }
}
