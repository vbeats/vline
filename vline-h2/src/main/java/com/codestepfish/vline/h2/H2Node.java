package com.codestepfish.vline.h2;

import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.h2.H2Properties;
import com.codestepfish.vline.h2.handler.H2ReadHandler;
import com.codestepfish.vline.h2.handler.H2WriteHandler;
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
public class H2Node extends Node {

    private H2ReadHandler h2ReadHandler;
    private H2WriteHandler h2WriteHandler;

    @Override
    public void init() {
        super.init();
        try {
            DataSourceInitializer.initDataSource(this);  // 数据源初始化

            H2Properties properties = this.getH2();

            switch (properties.getMode()) {
                case READ -> {
                    Assert.hasText(properties.getDataHandler(), "【" + this.getName() + "】 Require Config DataHandler");
                    Class<? extends H2ReadHandler> readHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(H2ReadHandler.class);
                    h2ReadHandler = readHandlerClazz.getDeclaredConstructor().newInstance();
                    Thread.ofVirtual().start(() -> h2ReadHandler.read(this));
                }
                case WRITE -> {
                    Assert.hasText(properties.getDataHandler(), "【" + this.getName() + "】 Require Config DataHandler");
                    Class<? extends H2WriteHandler> writeHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(H2WriteHandler.class);
                    h2WriteHandler = writeHandlerClazz.getDeclaredConstructor().newInstance();
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
        Thread.ofVirtual().start(() -> h2WriteHandler.write(this, data));
    }
}
