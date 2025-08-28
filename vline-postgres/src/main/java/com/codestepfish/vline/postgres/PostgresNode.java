package com.codestepfish.vline.postgres;

import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.postgres.PostgresProperties;
import com.codestepfish.vline.postgres.handler.PostgresReadHandler;
import com.codestepfish.vline.postgres.handler.PostgresWriteHandler;
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
public class PostgresNode extends Node {

    private PostgresReadHandler postgresReadHandler;
    private PostgresWriteHandler postgresWriteHandler;

    @Override
    public void init() {
        super.init();
        try {
            DataSourceInitializer.initDataSource(this);  // 数据源初始化

            PostgresProperties properties = this.getPostgres();

            switch (properties.getMode()) {
                case READ -> {
                    Assert.hasText(properties.getDataHandler(), "【" + this.getName() + "】 Require Config DataHandler");
                    Class<? extends PostgresReadHandler> readHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(PostgresReadHandler.class);
                    postgresReadHandler = readHandlerClazz.getDeclaredConstructor().newInstance();
                    Thread.ofVirtual().start(() -> postgresReadHandler.read(this));
                }
                case WRITE -> {
                    Assert.hasText(properties.getDataHandler(), "【" + this.getName() + "】 Require Config DataHandler");
                    Class<? extends PostgresWriteHandler> writeHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(PostgresWriteHandler.class);
                    postgresWriteHandler = writeHandlerClazz.getDeclaredConstructor().newInstance();
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
        Thread.ofVirtual().start(() -> postgresWriteHandler.write(this, data));
    }
}
