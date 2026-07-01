package com.codestepfish.vline.etcd;

import cn.hutool.extra.spring.SpringUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.etcd.EtcdProperties;
import com.codestepfish.vline.etcd.handler.EtcdDataHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.etcd.jetcd.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class EtcdNode extends Node {

    private EtcdDataHandler etcdDataHandler;

    @Override
    public void init() {
        super.init();

        EtcdProperties properties = this.getEtcd();
        try {
            EtcdClientHolder.ETCD_CLIENTS.put(this.getName(), Client.builder().endpoints(properties.getEndpoints()).build());

            etcdDataHandler = SpringUtil.getBean(EtcdDataHandler.class);

            log.info("【{}】 Etcd Node Init Success , Endpoints: {}", this.getName(), properties.getEndpoints());

            etcdDataHandler.init(this);
        } catch (Exception e) {
            log.error("【{}】 Etcd Node Init Error", this.getName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        EtcdClientHolder.ETCD_CLIENTS.forEach((key, value) -> value.close());
    }

    @Override
    public void receiveData(Object data) {
        etcdDataHandler.rec(this, data);
    }
}
