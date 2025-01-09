package com.codestepfish.vline.mssql2008r2;

import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.mssql.MssqlProperties;
import com.codestepfish.vline.mssql2008r2.handler.MssqlReadHandler;
import com.codestepfish.vline.mssql2008r2.handler.MssqlWriteHandler;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.anyline.data.datasource.DataSourceHolder;
import org.anyline.metadata.type.DatabaseType;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@Accessors(chain = true)
public class MssqlNode<T> extends Node<T> {

    MssqlReadHandler mssqlReadHandler;
    MssqlWriteHandler mssqlWriteHandler;

    @Override
    public void init() {
        super.init();
        try {
            initDs(this);  // 数据源初始化

            Assert.hasText(this.getMssql().getDataHandler(), "mssql dataHandler is null");

            Class<MssqlReadHandler> readHandlerClazz = (Class<MssqlReadHandler>) Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(this.getMssql().getDataHandler());
            Class<MssqlWriteHandler> writeHandlerClazz = (Class<MssqlWriteHandler>) Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(this.getMssql().getDataHandler());

            mssqlReadHandler = readHandlerClazz.getDeclaredConstructor().newInstance();
            mssqlWriteHandler = writeHandlerClazz.getDeclaredConstructor().newInstance();

            if (this.getMssql().getMode().equals(MssqlProperties.Mode.READ)) {
                // 开始读取数据
                mssqlReadHandler.read(this);
            }
        } catch (Exception e) {
            log.error("mssql : {} init failed : ", this.getName(), e);
            throw new RuntimeException(e);
        }
    }

    private void initDs(MssqlNode node) throws Exception {
        MssqlProperties properties = node.getMssql();

        if (!StringUtils.hasText(properties.getJdbcUrl())) {
            properties.setJdbcUrl(String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=%s;trustServerCertificate=%s", properties.getHost(), properties.getPort(), properties.getDatabaseName(), properties.getEncrypt(), properties.getTrustServerCertificate()));
        }

        Map<String, Object> params = new HashMap();
        params.put("url", properties.getJdbcUrl());
        params.put("type", "com.zaxxer.hikari.HikariDataSource");
        params.put("driver-class-name", properties.getDriverClassName());
        params.put("username", properties.getUsername());
        params.put("password", properties.getPassword());

        DataSourceHolder.reg(node.getName(), params, DatabaseType.MSSQL);
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
        DataSourceHolder.destroy(this.getName());
    }

    @Override
    public void sendData(T data) {
        mssqlWriteHandler.write(this, data);
    }
}
