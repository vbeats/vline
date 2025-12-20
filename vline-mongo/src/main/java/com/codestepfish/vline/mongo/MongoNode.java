package com.codestepfish.vline.mongo;

import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.mongo.MongoProperties;
import com.codestepfish.vline.mongo.handler.MongoReadHandler;
import com.codestepfish.vline.mongo.handler.MongoWriteHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mongodb.client.MongoClients;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.anyline.data.datasource.DataSourceHolder;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.Objects;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class MongoNode extends Node {

    private MongoReadHandler mongoReadHandler;
    private MongoWriteHandler mongoWriteHandler;

    @Override
    public void init() {
        super.init();
        try {
            MongoProperties properties = this.getMongo();

            // init mongo client
            MongoClientHolder.MONGO_CLIENTS.put(this.getName(), MongoClients.create(properties.getUri()));

            switch (properties.getMode()) {
                case READ -> {
                    Assert.hasText(properties.getDataHandler(), "【" + this.getName() + "】 Require Config DataHandler");
                    Class<? extends MongoReadHandler> readHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(MongoReadHandler.class);
                    mongoReadHandler = readHandlerClazz.getDeclaredConstructor().newInstance();
                    Thread.ofVirtual().start(() -> mongoReadHandler.read(this));
                }
                case WRITE -> {
                    Assert.hasText(properties.getDataHandler(), "【" + this.getName() + "】 Require Config DataHandler");
                    Class<? extends MongoWriteHandler> writeHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(MongoWriteHandler.class);
                    mongoWriteHandler = writeHandlerClazz.getDeclaredConstructor().newInstance();
                }
                case OTHER -> {
                    // do nothing
                }
            }

        } catch (Exception e) {
            log.error("【{}】 Init Failed : ", this.getName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            DataSourceHolder.destroy(this.getName());
        } catch (Exception e) {
            log.error("【{}】 Destroy Exception : ", this.getName(), e);
        }
    }

    @Override
    public <T> void receiveData(T data) {
        Thread.ofVirtual().start(() -> mongoWriteHandler.write(this, data));
    }
}
