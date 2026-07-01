package com.codestepfish.vline.mongo.handler;

import com.codestepfish.vline.mongo.MongoNode;

public interface MongoDataHandler {

    void init(MongoNode node);

    void rec(MongoNode node, Object data);
}
