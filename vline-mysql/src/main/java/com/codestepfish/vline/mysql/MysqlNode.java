package com.codestepfish.vline.mysql;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.mysql.MysqlProperties;
import com.codestepfish.vline.mysql.handler.MysqlReadHandler;
import com.codestepfish.vline.mysql.handler.MysqlWriteHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class MysqlNode extends Node {

    private MysqlReadHandler mysqlReadHandler;
    private MysqlWriteHandler mysqlWriteHandler;

    @Override
    public void init() {
        super.init();
        try {
            DataSourceInitializer.initDataSource(this);  // 数据源初始化

            MysqlProperties properties = this.getMysql();

            switch (properties.getMode()) {
                case READ -> {
                    Assert.hasText(properties.getDataHandler(), "【" + this.getName() + "】 Require Config DataHandler");
                    Class<? extends MysqlReadHandler> readHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(MysqlReadHandler.class);
                    mysqlReadHandler = readHandlerClazz.getDeclaredConstructor().newInstance();
                    ThreadUtil.execute(() -> mysqlReadHandler.read(this));
                }
                case WRITE -> {
                    Assert.hasText(properties.getDataHandler(), "【" + this.getName() + "】 Require Config DataHandler");
                    Class<? extends MysqlWriteHandler> writeHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(MysqlWriteHandler.class);
                    mysqlWriteHandler = writeHandlerClazz.getDeclaredConstructor().newInstance();
                }
                case OTHER -> {
                    // do nothing
                }
            }

        } catch (Exception e) {
            log.error("【{}】 Init Failed : ", this.getName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            DataSourceHolder.destroy(this.getName());
        } catch (Exception e) {
            log.error("【{}】 Destroy Exception : ", this.getName(), e);
        }
    }

    @Override
    public <T> void receiveData(T data) {
        ThreadUtil.execute(() -> mysqlWriteHandler.write(this, data));
    }
}
