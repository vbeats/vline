package com.codestepfish.vline.mongo.handler;

import com.codestepfish.vline.mongo.MongoNode;

public interface MongoDataHandler {

    void handle(MongoNode node, Object data);
}
