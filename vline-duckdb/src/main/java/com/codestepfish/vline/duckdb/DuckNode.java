package com.codestepfish.vline.duckdb;

import cn.hutool.extra.spring.SpringUtil;
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

import java.sql.DriverManager;

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

            duckDataHandler = SpringUtil.getBean(DuckDataHandler.class);

            log.info("【{}】 Duck Node Init Success ...", this.getName());

            duckDataHandler.init(this);
        } catch (Exception e) {
            log.error("【{}】 Init Failed : ", this.getName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        duckDataHandler.destroy(this);
    }

    @Override
    public void receiveData(Object data) {
        duckDataHandler.rec(this, data);
    }
}
