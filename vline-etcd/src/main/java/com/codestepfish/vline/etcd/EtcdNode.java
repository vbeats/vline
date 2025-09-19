package com.codestepfish.vline.etcd;

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
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.Objects;

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
            Assert.hasText(properties.getDataHandler(), "【" + this.getName() + "】 Require Config DataHandler");
            Class<? extends EtcdDataHandler> readHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(EtcdDataHandler.class);
            etcdDataHandler = readHandlerClazz.getDeclaredConstructor().newInstance();

            EtcdClientHolder.ETCD_CLIENTS.put(this.getName(), Client.builder().endpoints(properties.getEndpoints()).build());
            log.info("【{}】 Etcd Node Init Success , Endpoints: {}", this.getName(), properties.getEndpoints());
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
    public <T> void receiveData(T data) {
        Thread.ofVirtual().start(() -> etcdDataHandler.handle(this, data));
    }
}
