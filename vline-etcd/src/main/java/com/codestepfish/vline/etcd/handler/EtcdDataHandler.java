package com.codestepfish.vline.etcd.handler;


import com.codestepfish.vline.etcd.EtcdNode;

public interface EtcdDataHandler {
    void handle(EtcdNode node, Object data);
}
