package com.codestepfish.vline.example.h2;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.h2.H2Node;
import com.codestepfish.vline.h2.handler.H2ReadHandler;
import lombok.extern.slf4j.Slf4j;
import org.anyline.entity.DataSet;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;

@Slf4j
public class H2ReadExampleHandler implements H2ReadHandler {
    @Override
    public void read(H2Node node) {

        AnylineService service = ServiceProxy.service(node.getName());

        while (true) {
            DataSet dataSet = service.querys("test");

            log.info("read data : {}", dataSet);

            ThreadUtil.safeSleep(2000L);
        }
    }
}
