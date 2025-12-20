package com.codestepfish.vline.example.mongo;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.core.VLineContext;
import com.codestepfish.vline.mongo.MongoClientHolder;
import com.codestepfish.vline.mongo.MongoNode;
import com.codestepfish.vline.mongo.handler.MongoReadHandler;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

@Slf4j
public class MongoReadExampleHandler implements MongoReadHandler {
    @Override
    public void read(MongoNode node) {

        while (true) {
            MongoClient mongoClient = MongoClientHolder.MONGO_CLIENTS.get(node.getName());

            MongoDatabase db = mongoClient.getDatabase("test");

            MongoCollection<Document> tTest = db.getCollection("t_test");

            FindIterable<Document> documents = tTest.find();

            documents.forEach(document -> {
                log.info("document: {}", document);
                VLineContext.posMsg(node.getName(), document);
            });

            ThreadUtil.safeSleep(2000L);
        }
    }
}
