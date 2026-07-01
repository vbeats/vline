package com.codestepfish.vline.etcd.handler;


import com.codestepfish.vline.etcd.EtcdNode;

public interface EtcdDataHandler {

    void init(EtcdNode node);

    void rec(EtcdNode node, Object data);
}
