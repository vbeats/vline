package com.codestepfish.vline.example.sqlite;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.spring.boot.starter.VLineContext;
import com.codestepfish.vline.sqlite.SqLiteNode;
import com.codestepfish.vline.sqlite.handler.SqLiteReadHandler;
import lombok.extern.slf4j.Slf4j;
import org.anyline.entity.DataSet;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;

@Slf4j
public class SqLiteReadExampleHandler implements SqLiteReadHandler {
    @Override
    public void read(SqLiteNode node) {

        AnylineService service = ServiceProxy.service(node.getName());

        while (true) {
            DataSet dataSet = service.querys("test");

            log.info("read data : {}", dataSet);
            VLineContext.posMsg(node.getName(), dataSet);

            ThreadUtil.safeSleep(2000L);
        }
    }
}
