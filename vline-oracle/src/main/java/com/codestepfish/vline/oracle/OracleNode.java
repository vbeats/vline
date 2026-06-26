package com.codestepfish.vline.oracle;

import cn.hutool.extra.spring.SpringUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.oracle.handler.OracleDataHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.anyline.data.datasource.DataSourceHolder;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class OracleNode extends Node {

    private OracleDataHandler oracleDataHandler;

    @Override
    public void init() {
        super.init();
        try {
            OracleDataSourceInitializer.initDataSource(this);  // 数据源初始化

            oracleDataHandler = SpringUtil.getBean(OracleDataHandler.class);
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
    public void receiveData(Object data) {
        oracleDataHandler.handle(this, data);
    }
}
