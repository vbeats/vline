package com.codestepfish.vline.example.mssql;

import com.codestepfish.vline.mssql2000.MssqlNode;
import com.codestepfish.vline.mssql2000.handler.MssqlWriteHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MssqlWriteExampleHandler<T> implements MssqlWriteHandler<T> {

    @Override
    public void write(MssqlNode node, T data) {
        log.info("==========> write ds 数据: {}", data);
    }
}
