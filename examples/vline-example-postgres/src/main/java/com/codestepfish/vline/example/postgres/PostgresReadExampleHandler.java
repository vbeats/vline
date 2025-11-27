package com.codestepfish.vline.example.postgres;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.postgres.PostgresNode;
import com.codestepfish.vline.postgres.handler.PostgresReadHandler;
import com.codestepfish.vline.core.VLineContext;
import lombok.extern.slf4j.Slf4j;
import org.anyline.entity.DataSet;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;

@Slf4j
public class PostgresReadExampleHandler implements PostgresReadHandler {
    @Override
    public void read(PostgresNode node) {

        AnylineService service = ServiceProxy.service(node.getName());

        while (true) {
            DataSet dataSet = service.querys("test");

            log.info("read data : {}", dataSet);
            VLineContext.posMsg(node.getName(), dataSet);

            ThreadUtil.safeSleep(2000L);
        }
    }
}
