package com.codestepfish.vline.mongo;

import cn.hutool.extra.spring.SpringUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.mongo.MongoProperties;
import com.codestepfish.vline.mongo.handler.MongoDataHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mongodb.client.MongoClients;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.anyline.data.datasource.DataSourceHolder;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class MongoNode extends Node {

    private MongoDataHandler mongoDataHandler;

    @Override
    public void init() {
        super.init();
        try {
            MongoProperties properties = this.getMongo();

            // init mongo client
            MongoClientHolder.MONGO_CLIENTS.put(this.getName(), MongoClients.create(properties.getUri()));

            mongoDataHandler = SpringUtil.getBean(MongoDataHandler.class);

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
    public void receiveData(Object data) {
        mongoDataHandler.handle(this, data);
    }
}
