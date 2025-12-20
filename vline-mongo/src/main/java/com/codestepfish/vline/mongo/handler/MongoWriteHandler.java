package com.codestepfish.vline.mongo.handler;

import com.codestepfish.vline.mongo.MongoNode;

public interface MongoWriteHandler {

    <T> void write(MongoNode node, T data); // write mode
}
