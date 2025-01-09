package com.codestepfish.vline.mssql2008r2;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSON;
import com.codestepfish.vline.core.Node;
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
@Accessors(chain = true)
public class MssqlNode<T> extends Node<T> {
    @Override
    public void init() {
        log.info("node init: {}", JSON.toJSONString(this));

        // 初始化数据源
        ThreadUtil.execute(() -> this.initDs(this));
    }

    private void initDs(MssqlNode<T> node) {

    }

    @Override
    public void destroy() throws Exception {
        log.info("node destroy: {}", this.getName());
        DataSourceHolder.destroy(this.getName());
    }

    @Override
    public void sendData(T data) {

    }
}
