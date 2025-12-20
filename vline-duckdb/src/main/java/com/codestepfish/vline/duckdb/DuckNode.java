package com.codestepfish.vline.duckdb;

import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.duckdb.DuckProperties;
import com.codestepfish.vline.duckdb.handler.DuckDataHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.duckdb.DuckDBConnection;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.sql.DriverManager;
import java.util.Objects;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class DuckNode extends Node {

    private DuckDataHandler duckDataHandler;

    @Override
    public void init() {
        super.init();
        try {
            DuckProperties properties = this.getDuckdb();

            Class.forName("org.duckdb.DuckDBDriver");
            DuckClientHolder.DUCK_CLIENTS.put(this.getName(), (DuckDBConnection) DriverManager.getConnection(properties.getUri()));

            Assert.hasText(properties.getDataHandler(), "【" + this.getName() + "】 Require Config DataHandler");
            Class<? extends DuckDataHandler> readHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(DuckDataHandler.class);
            duckDataHandler = readHandlerClazz.getDeclaredConstructor().newInstance();

            log.info("【{}】 Duck Node Init Success ...", this.getName());
        } catch (Exception e) {
            log.error("【{}】 Init Failed : ", this.getName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public <T> void receiveData(T data) {
        Thread.ofVirtual().start(() -> duckDataHandler.handle(this, data));
    }
}
