package com.codestepfish.vline.mssql2008r2;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.mssql2008r2.handler.MssqlReadHandler;
import com.codestepfish.vline.mssql2008r2.handler.MssqlWriteHandler;
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
public class MssqlNode<T> extends Node<T> {

    MssqlReadHandler mssqlReadHandler;
    MssqlWriteHandler mssqlWriteHandler;

    @Override
    public void init() {
        super.init();
        try {
            DataSourceInitializer.initDataSource(this);  // 数据源初始化

            Assert.hasText(this.getMssql().getDataHandler(), "mssql dataHandler is null");

            switch (this.getMssql().getMode()) {
                case READ -> {
                    Class<MssqlReadHandler> readHandlerClazz = (Class<MssqlReadHandler>) Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(this.getMssql().getDataHandler());
                    mssqlReadHandler = readHandlerClazz.getDeclaredConstructor().newInstance();
                    ThreadUtil.execute(() -> mssqlReadHandler.read(this));
                }
                case WRITE -> {
                    Class<MssqlWriteHandler> writeHandlerClazz = (Class<MssqlWriteHandler>) Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(this.getMssql().getDataHandler());
                    mssqlWriteHandler = writeHandlerClazz.getDeclaredConstructor().newInstance();
                }
            }

        } catch (Exception e) {
            log.error("mssql : {} init failed : ", this.getName(), e);
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
        ThreadUtil.execute(() -> mssqlWriteHandler.write(this, data));
    }
}
