package com.codestepfish.vline.core;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import com.codestepfish.vline.core.http.HttpProperties;
import com.codestepfish.vline.core.redis.RedisProperties;
import com.codestepfish.vline.core.tcp.TcpProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Slf4j
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class Node<T> implements INode<T>, Serializable {

    @Serial
    private static final long serialVersionUID = 7681638777205375687L;

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)
    NodeType type;

    String name; // 设备名称  全局唯一

    List<String> tags;  // 节点标签

    // -------------------------不同通信协议配置属性    一个 node 只能配置一种
    TcpProperties tcp;

    HttpProperties http;

    RedisProperties redis;

    @Override
    public void init() {
        log.info("node: {}", JSON.toJSONString(this));
    }

    @Override
    public void destroy() {
        log.info("node : {}", this.getName());
    }

    @Override
    public void sendMsg(T msg) {

    }

    public void setTcp(TcpProperties tcp) {
        this.tcp = tcp;
        if (!ObjectUtils.isEmpty(tcp)) {
            this.type = NodeType.TCP;
        }
    }

    public void setHttp(HttpProperties http) {
        this.http = http;
        if (!ObjectUtils.isEmpty(http)) {
            this.type = NodeType.HTTP;
        }
    }

    public void setRedis(RedisProperties redis) {
        this.redis = redis;
        if (!ObjectUtils.isEmpty(redis)) {
            this.type = NodeType.REDIS;
        }
    }
}


