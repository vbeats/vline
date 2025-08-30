package com.codestepfish.vline.oracle;

import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.oracle.OracleProperties;
import com.codestepfish.vline.oracle.handler.OracleReadHandler;
import com.codestepfish.vline.oracle.handler.OracleWriteHandler;
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
public class OracleNode extends Node {

    private OracleReadHandler oracleReadHandler;
    private OracleWriteHandler oracleWriteHandler;

    @Override
    public void init() {
        super.init();
        try {
            OracleDataSourceInitializer.initDataSource(this);  // 数据源初始化

            OracleProperties properties = this.getOracle();

            switch (properties.getMode()) {
                case READ -> {
                    Assert.hasText(properties.getDataHandler(), "【" + this.getName() + "】 Require Config DataHandler");
                    Class<? extends OracleReadHandler> readHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(OracleReadHandler.class);
                    oracleReadHandler = readHandlerClazz.getDeclaredConstructor().newInstance();
                    Thread.ofVirtual().start(() -> oracleReadHandler.read(this));
                }
                case WRITE -> {
                    Assert.hasText(properties.getDataHandler(), "【" + this.getName() + "】 Require Config DataHandler");
                    Class<? extends OracleWriteHandler> writeHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(OracleWriteHandler.class);
                    oracleWriteHandler = writeHandlerClazz.getDeclaredConstructor().newInstance();
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
        Thread.ofVirtual().start(() -> oracleWriteHandler.write(this, data));
    }
}
