package com.codestepfish.vline.example.mssql;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.mssql2008r2.MssqlNode;
import com.codestepfish.vline.mssql2008r2.handler.MssqlReadHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MssqlReadExampleHandler implements MssqlReadHandler {
    @Override
    public void read(MssqlNode node) {
        while (true) {
            // todo 读 test 表数据

            log.info("read data : {}", node);
//            VLineContext.posMsg(node.getName(), "hello world");

            ThreadUtil.safeSleep(2000L);
        }
    }
}
