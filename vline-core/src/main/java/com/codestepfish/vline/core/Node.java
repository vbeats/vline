package com.codestepfish.vline.core;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import com.codestepfish.vline.core.enums.NodeType;
import com.codestepfish.vline.core.http.HttpProperties;
import com.codestepfish.vline.core.modbus.ModbusProperties;
import com.codestepfish.vline.core.mssql.MssqlProperties;
import com.codestepfish.vline.core.mysql.MysqlProperties;
import com.codestepfish.vline.core.oracle.OracleProperties;
import com.codestepfish.vline.core.postgres.PostgresProperties;
import com.codestepfish.vline.core.redis.RedisProperties;
import com.codestepfish.vline.core.serialport.SerialPortProperties;
import com.codestepfish.vline.core.sqlite.SqliteProperties;
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
import java.util.Map;

@Getter
@Setter
@Slf4j
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class Node implements INode, Serializable {

    @Serial
    private static final long serialVersionUID = 7681638777205375687L;

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)
    private NodeType type;

    private String name; // 设备名称  全局唯一

    private List<Map<String, Object>> extra;  // 节点 扩展属性  业务自定义

    // -------------------------不同通信协议配置属性    一个 node 只能配置一种
    private TcpProperties tcp;

    private HttpProperties http;

    private RedisProperties redis;

    private MssqlProperties mssql;

    private MssqlProperties mssql2000;

    private MysqlProperties mysql;

    private PostgresProperties postgres;

    private OracleProperties oracle;

    private SqliteProperties sqlite;

    private SerialPortProperties serialPort;

    private ModbusProperties modbus;

    @Override
    public void init() {
        log.info("Node:【{}】 Start Init...", this.getName());
    }

    @Override
    public void destroy() {
        log.info("【{}】 Destroy ...", this.getName());
    }

    @Override
    public <T> void receiveData(T data) {
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

    public void setMssql(MssqlProperties mssql) {
        this.mssql = mssql;
        if (!ObjectUtils.isEmpty(mssql)) {
            this.type = NodeType.MSSQL;
        }
    }

    public void setMysql(MysqlProperties mysql) {
        this.mysql = mysql;
        if (!ObjectUtils.isEmpty(mysql)) {
            this.type = NodeType.MYSQL;
        }
    }

    public void setMssql2000(MssqlProperties mssql2000) {
        this.mssql2000 = mssql2000;
        if (!ObjectUtils.isEmpty(mssql2000)) {
            this.type = NodeType.MSSQL2000;
        }
    }

    public void setSqlite(SqliteProperties sqlite) {
        this.sqlite = sqlite;
        if (!ObjectUtils.isEmpty(sqlite)) {
            this.type = NodeType.SQLITE;
        }
    }

    public void setPostgres(PostgresProperties postgres) {
        this.postgres = postgres;
        if (!ObjectUtils.isEmpty(postgres)) {
            this.type = NodeType.POSTGRES;
        }
    }

    public void setSerialPort(SerialPortProperties serialPort) {
        this.serialPort = serialPort;
        if (!ObjectUtils.isEmpty(serialPort)) {
            this.type = NodeType.SERIAL_PORT;
        }
    }

    public void setOracle(OracleProperties oracle) {
        this.oracle = oracle;
        if (!ObjectUtils.isEmpty(oracle)) {
            this.type = NodeType.ORACLE;
        }
    }

    public void setModbus(ModbusProperties modbus) {
        this.modbus = modbus;
        if (!ObjectUtils.isEmpty(modbus)) {
            this.type = NodeType.MODBUS;
        }
    }
}


