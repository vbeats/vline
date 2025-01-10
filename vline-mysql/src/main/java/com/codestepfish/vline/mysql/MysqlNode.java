package com.codestepfish.vline.mysql;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.mysql.MysqlProperties;
import com.codestepfish.vline.mysql.handler.MysqlReadHandler;
import com.codestepfish.vline.mysql.handler.MysqlWriteHandler;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.anyline.data.datasource.DataSourceHolder;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.Objects;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@Accessors(chain = true)
public class MysqlNode<T> extends Node<T> {

    MysqlReadHandler mysqlReadHandler;
    MysqlWriteHandler mysqlWriteHandler;

    @Override
    public void init() {
        super.init();
        try {
            DataSourceInitializer.initDataSource(this);  // 数据源初始化

            MysqlProperties properties = this.getMysql();

            Assert.hasText(properties.getDataHandler(), "mysql dataHandler is null");

            switch (properties.getMode()) {
                case READ -> {
                    Class<MysqlReadHandler> readHandlerClazz = (Class<MysqlReadHandler>) Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler());
                    mysqlReadHandler = readHandlerClazz.getDeclaredConstructor().newInstance();
                    ThreadUtil.execute(() -> mysqlReadHandler.read(this));
                }
                case WRITE -> {
                    Class<MysqlWriteHandler> writeHandlerClazz = (Class<MysqlWriteHandler>) Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler());
                    mysqlWriteHandler = writeHandlerClazz.getDeclaredConstructor().newInstance();
                }
            }

        } catch (Exception e) {
            log.error("mysql : {} init failed : ", this.getName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
        DataSourceHolder.destroy(this.getName());
    }

    @Override
    public void sendData(T data) {
        ThreadUtil.execute(() -> mysqlWriteHandler.write(this, data));
    }
}
