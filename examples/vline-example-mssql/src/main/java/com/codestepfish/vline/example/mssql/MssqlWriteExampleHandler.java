package com.codestepfish.vline.example.mssql;

import com.codestepfish.vline.mssql2000.MssqlNode;
import com.codestepfish.vline.mssql2000.handler.MssqlWriteHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MssqlWriteExampleHandler implements MssqlWriteHandler {

    @Override
    public <T> void write(MssqlNode node, T data) {
        log.info("==========> write ds 数据: {}", data);
    }
}
