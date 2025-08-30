package com.codestepfish.vline.example.oracle;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.oracle.OracleNode;
import com.codestepfish.vline.oracle.handler.OracleReadHandler;
import com.codestepfish.vline.spring.boot.starter.VLineContext;
import lombok.extern.slf4j.Slf4j;
import org.anyline.entity.DataSet;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;

@Slf4j
public class OracleReadExampleHandler implements OracleReadHandler {
    @Override
    public void read(OracleNode node) {

        AnylineService service = ServiceProxy.service(node.getName());

        while (true) {
            DataSet dataSet = service.querys("BS_TEST");

            log.info("read data : {}", dataSet);
            VLineContext.posMsg(node.getName(), dataSet);

            ThreadUtil.safeSleep(2000L);
        }
    }
}
