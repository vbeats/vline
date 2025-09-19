package com.codestepfish.vline.etcd.handler;


import com.codestepfish.vline.etcd.EtcdNode;

public interface EtcdDataHandler {
    <T> void handle(EtcdNode node, T data);
}
