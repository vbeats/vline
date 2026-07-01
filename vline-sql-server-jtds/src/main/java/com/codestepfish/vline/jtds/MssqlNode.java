package com.codestepfish.vline.jtds;

import cn.hutool.extra.spring.SpringUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.jtds.handler.MssqlDataHandler;
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
public class MssqlNode extends Node {

    private MssqlDataHandler mssqlDataHandler;

    @Override
    public void init() {
        super.init();
        try {
            DataSourceInitializer.initDataSource(this);  // 数据源初始化

            mssqlDataHandler = SpringUtil.getBean(MssqlDataHandler.class);
            mssqlDataHandler.init(this);
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
        mssqlDataHandler.rec(this, data);
    }
}
