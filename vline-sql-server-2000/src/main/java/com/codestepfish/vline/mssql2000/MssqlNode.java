package com.codestepfish.vline.mssql2000;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.mssql.MssqlProperties;
import com.codestepfish.vline.mssql2000.handler.MssqlReadHandler;
import com.codestepfish.vline.mssql2000.handler.MssqlWriteHandler;
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
public class MssqlNode extends Node {

    private MssqlReadHandler mssqlReadHandler;
    private MssqlWriteHandler mssqlWriteHandler;

    @Override
    public void init() {
        super.init();
        try {
            DataSourceInitializer.initDataSource(this);  // 数据源初始化

            MssqlProperties properties = this.getMssql2000();

            switch (properties.getMode()) {
                case READ -> {
                    Assert.hasText(properties.getDataHandler(), "【 " + this.getName() + " 】 Require Config DataHandler");
                    Class<? extends MssqlReadHandler> readHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(MssqlReadHandler.class);
                    mssqlReadHandler = readHandlerClazz.getDeclaredConstructor().newInstance();
                    ThreadUtil.execute(() -> mssqlReadHandler.read(this));
                }
                case WRITE -> {
                    Assert.hasText(properties.getDataHandler(), "【 " + this.getName() + " 】 Require Config DataHandler");
                    Class<? extends MssqlWriteHandler> writeHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(MssqlWriteHandler.class);
                    mssqlWriteHandler = writeHandlerClazz.getDeclaredConstructor().newInstance();
                }
                case OTHER -> {
                    // do nothing
                }
            }

        } catch (Exception e) {
            log.error("【 {} 】 Init Failed : ", this.getName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            DataSourceHolder.destroy(this.getName());
        } catch (Exception e) {
            log.error("【 {} 】 Destroy Exception : ", this.getName(), e);
        }
    }

    @Override
    public <T> void receiveData(T data) {
        ThreadUtil.execute(() -> mssqlWriteHandler.write(this, data));
    }
}
