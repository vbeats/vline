package com.codestepfish.vline.example.mysql;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.mysql.MysqlNode;
import com.codestepfish.vline.mysql.handler.MysqlReadHandler;
import com.codestepfish.vline.core.VLineContext;
import lombok.extern.slf4j.Slf4j;
import org.anyline.entity.DataSet;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;

@Slf4j
public class MysqlReadExampleHandler implements MysqlReadHandler {
    @Override
    public void read(MysqlNode node) {

        AnylineService service = ServiceProxy.service(node.getName());

        while (true) {
            DataSet dataSet = service.querys("test");

            log.info("read data : {}", dataSet);
            VLineContext.posMsg(node.getName(), dataSet);

            ThreadUtil.safeSleep(2000L);
        }
    }
}
