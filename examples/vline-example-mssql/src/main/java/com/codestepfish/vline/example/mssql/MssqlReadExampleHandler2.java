package com.codestepfish.vline.example.mssql;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.mssql.MssqlNode;
import com.codestepfish.vline.mssql.handler.MssqlReadHandler;
import com.codestepfish.vline.core.VLineContext;
import lombok.extern.slf4j.Slf4j;
import org.anyline.entity.DataSet;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;

@Slf4j
public class MssqlReadExampleHandler2 implements MssqlReadHandler {
    @Override
    public void read(MssqlNode node) {

        AnylineService service = ServiceProxy.service(node.getName());

        while (true) {
            DataSet dataSet = service.querys("dbo.test");

            log.info("read data : {}", dataSet);
            VLineContext.posMsg(node.getName(), dataSet);

            ThreadUtil.safeSleep(2000L);
        }
    }
}
