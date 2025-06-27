package com.codestepfish.vline.sqlite;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.sqlite.SqliteProperties;
import com.codestepfish.vline.sqlite.handler.SqLiteReadHandler;
import com.codestepfish.vline.sqlite.handler.SqLiteWriteHandler;
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
public class SqLiteNode<T> extends Node<T> {

    SqLiteReadHandler sqliteReadHandler;
    SqLiteWriteHandler sqliteWriteHandler;

    @Override
    public void init() {
        super.init();
        try {
            DataSourceInitializer.initDataSource(this);  // 数据源初始化

            SqliteProperties properties = this.getSqlite();

            switch (properties.getMode()) {
                case READ -> {
                    Assert.hasText(properties.getDataHandler(), "sqlite dataHandler is null");
                    Class<? extends SqLiteReadHandler> readHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(SqLiteReadHandler.class);
                    sqliteReadHandler = readHandlerClazz.getDeclaredConstructor().newInstance();
                    ThreadUtil.execute(() -> sqliteReadHandler.read(this));
                }
                case WRITE -> {
                    Assert.hasText(properties.getDataHandler(), "sqlite dataHandler is null");
                    Class<? extends SqLiteWriteHandler> writeHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(SqLiteWriteHandler.class);
                    sqliteWriteHandler = writeHandlerClazz.getDeclaredConstructor().newInstance();
                }
                case OTHER -> {
                    // do nothing
                }
            }

        } catch (Exception e) {
            log.error("sqlite : {} init failed : ", this.getName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            DataSourceHolder.destroy(this.getName());
        } catch (Exception e) {
            log.error("========> sqlite : {} destroy failed : ", this.getName(), e);
        }
    }

    @Override
    public void receiveData(T data) {
        ThreadUtil.execute(() -> sqliteWriteHandler.write(this, data));
    }
}
