package com.codestepfish.vline.mongo;

import com.mongodb.client.MongoClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MongoClientHolder {

    // node name -> mongo client
    public static final Map<String, MongoClient> MONGO_CLIENTS = new ConcurrentHashMap<>(1);
}
