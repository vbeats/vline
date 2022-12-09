package com.codestepfish.vline.core;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import com.codestepfish.vline.core.database.DataBaseProperties;
import com.codestepfish.vline.core.etcd.EtcdProperties;
import com.codestepfish.vline.core.grpc.GrpcProperties;
import com.codestepfish.vline.core.http.HttpProperties;
import com.codestepfish.vline.core.mqtt.MqttProperties;
import com.codestepfish.vline.core.redis.RedisProperties;
import com.codestepfish.vline.core.serial_port.SerialPortProperties;
import com.codestepfish.vline.core.tcp.TcpProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Slf4j
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class Node<T> implements INode<T>, Serializable {

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)
    NodeType type;

    String name;

    List<String> tags;

    // -------------------------不同通信协议配置属性    一个 node 只能配置一种
    TcpProperties tcp;

    HttpProperties http;

    RedisProperties redis;

    EtcdProperties etcd;

    GrpcProperties grpc;

    MqttProperties mqtt;

    SerialPortProperties serialPort;

    DataBaseProperties database;

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

    public void setEtcd(EtcdProperties etcd) {
        this.etcd = etcd;
        if (!ObjectUtils.isEmpty(etcd)) {
            this.type = NodeType.ETCD;
        }
    }

    public void setGrpc(GrpcProperties grpc) {
        this.grpc = grpc;
        if (!ObjectUtils.isEmpty(grpc)) {
            this.type = NodeType.GRPC;
        }
    }

    public void setMqtt(MqttProperties mqtt) {
        this.mqtt = mqtt;
        if (!ObjectUtils.isEmpty(mqtt)) {
            this.type = NodeType.MQTT;
        }
    }

    public void setSerialPort(SerialPortProperties serialPort) {
        this.serialPort = serialPort;
        if (!ObjectUtils.isEmpty(serialPort)) {
            this.type = NodeType.SERIAL_PORT;
        }
    }

    public void setDatabase(DataBaseProperties database) {
        this.database = database;
        if (!ObjectUtils.isEmpty(database)) {
            this.type = NodeType.DATABASE;
        }
    }
}


