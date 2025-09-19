package com.codestepfish.vline.example.etcd;

import com.codestepfish.vline.etcd.EtcdNode;
import com.codestepfish.vline.etcd.handler.EtcdDataHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EtcdExampleDataHandler implements EtcdDataHandler {
    @Override
    public <T> void handle(EtcdNode node, T data) {
        log.info("===========> node: {} rec data: {}", node.getName(), data);
    }
}
