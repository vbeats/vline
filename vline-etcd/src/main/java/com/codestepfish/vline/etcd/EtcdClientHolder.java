package com.codestepfish.vline.etcd;

import io.etcd.jetcd.Client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EtcdClientHolder {

    // node name -> etcd client
    public static final Map<String, Client> ETCD_CLIENTS = new ConcurrentHashMap<>(1);
}
