# vline

多数据源 数据采集、传输中间件, 轻量级 边缘设备场景 ⚓

Not ETL

![alt text](/images/vline.svg)

## dependency

> JDK21 springboot3.x 其它版本未测试

```gradle
    implementation project("com.codestepfish.vline:vline-spring-boot-starter:${version}")
    
    // 对应数据源依赖
    implementation project("com.codestepfish.vline:vline-tcp:${version}")
    implementation project("com.codestepfish.vline:vline-http:${version}")
    .....
```

## 💻 dev

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/vbeats/vline)

[![zread](https://img.shields.io/badge/Ask_Zread-_.svg?style=flat&color=00b0aa&labelColor=000000&logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPHN2ZyB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIHZpZXdCb3g9IjAgMCAxNiAxNiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTQuOTYxNTYgMS42MDAxSDIuMjQxNTZDMS44ODgxIDEuNjAwMSAxLjYwMTU2IDEuODg2NjQgMS42MDE1NiAyLjI0MDFWNC45NjAxQzEuNjAxNTYgNS4zMTM1NiAxLjg4ODEgNS42MDAxIDIuMjQxNTYgNS42MDAxSDQuOTYxNTZDNS4zMTUwMiA1LjYwMDEgNS42MDE1NiA1LjMxMzU2IDUuNjAxNTYgNC45NjAxVjIuMjQwMUM1LjYwMTU2IDEuODg2NjQgNS4zMTUwMiAxLjYwMDEgNC45NjE1NiAxLjYwMDFaIiBmaWxsPSIjZmZmIi8%2BCjxwYXRoIGQ9Ik00Ljk2MTU2IDEwLjM5OTlIMi4yNDE1NkMxLjg4ODEgMTAuMzk5OSAxLjYwMTU2IDEwLjY4NjQgMS42MDE1NiAxMS4wMzk5VjEzLjc1OTlDMS42MDE1NiAxNC4xMTM0IDEuODg4MSAxNC4zOTk5IDIuMjQxNTYgMTQuMzk5OUg0Ljk2MTU2QzUuMzE1MDIgMTQuMzk5OSA1LjYwMTU2IDE0LjExMzQgNS42MDE1NiAxMy43NTk5VjExLjAzOTlDNS42MDE1NiAxMC42ODY0IDUuMzE1MDIgMTAuMzk5OSA0Ljk2MTU2IDEwLjM5OTlaIiBmaWxsPSIjZmZmIi8%2BCjxwYXRoIGQ9Ik0xMy43NTg0IDEuNjAwMUgxMS4wMzg0QzEwLjY4NSAxLjYwMDEgMTAuMzk4NCAxLjg4NjY0IDEwLjM5ODQgMi4yNDAxVjQuOTYwMUMxMC4zOTg0IDUuMzEzNTYgMTAuNjg1IDUuNjAwMSAxMS4wMzg0IDUuNjAwMUgxMy43NTg0QzE0LjExMTkgNS42MDAxIDE0LjM5ODQgNS4zMTM1NiAxNC4zOTg0IDQuOTYwMVYyLjI0MDFDMTQuMzk4NCAxLjg4NjY0IDE0LjExMTkgMS42MDAxIDEzLjc1ODQgMS42MDAxWiIgZmlsbD0iI2ZmZiIvPgo8cGF0aCBkPSJNNCAxMkwxMiA0TDQgMTJaIiBmaWxsPSIjZmZmIi8%2BCjxwYXRoIGQ9Ik00IDEyTDEyIDQiIHN0cm9rZT0iI2ZmZiIgc3Ryb2tlLXdpZHRoPSIxLjUiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIvPgo8L3N2Zz4K&logoColor=ffffff)](https://zread.ai/vbeats/vline)

## todo

- [x] init sql 改造, 通过flyway实现版本控制
- [ ] gradle registerFeature改造 || 自定义条件注解
- [ ] add thymeleaf yaml editor & config持久化

## Support

| module          | remark  | progress |
|-----------------|---------|----------|
| tcp             |         | ✅        |
| http            |         | ✅        |
| redis           |         | ✅        |
| mysql           |         | ✅        |
| sqlite          |         | ✅        |
| postgresql      |         | ✅        |
| sql-server      | mssql驱动 | ✅        |
| sql-server-jtds | jtds驱动  | ✅        |
| oracle          | 11gr2 + | ✅        |
| etcd            |         | ✅        |
| serial-port     |         | ✅        |
| h2              |         | ✅        |

## desc

1. ~~msg data持久化 保证数据传递不丢失~~  由上层应用层实现

## module

`database` 数据库相关操作 基于`anyline`

| module                    | desc                                           |
|---------------------------|------------------------------------------------|
| vline-core                | 定义Node基本信息、属性, Node可以理解为入口/出口对应的数据源            |
| vline-tcp                 | netty tcp                                      |
| vline-http                | 自定义实现                                          |
| vline-redis               | 注入redisson client                              |
| vline-mysql               | mysql8 其它未测试                                   |
| vline-postgres            | postgresql                                     |
| vline-sqlite              | sqlite                                         |
| vline-sql-server-jtds     | jtds驱动 2000 ~ 2012                             |
| vline-sql-server          | mssql驱动 2005+                                  |
| vline-oracle              | oracle 11gr2 +                                 |
| vline-etcd                | etcd client                                    |
| vline-h2                  | h2数据库                                          |
| vline-serial-port         | 串口通信                                           |
| vline-spring-boot-starter | spring boot starter : yml解析 初始化  event bus事件监听 |
| examples                  | 示例                                             |

## yaml config

> com.codestepfish.vline.spring.boot.starter.VLineProperties

### basic config

```yaml
vline:
  cache-stats: false
  nodes:
    - name: t1
      type: tcp
      extra:
        - xx: oo
      # protocol:
      #  - property:
      #  - property:
  struct:
    t1:
      - t2
```

| key         | 必填 | desc                                                                         |
|:------------|----|------------------------------------------------------------------------------|
| cache-stats | N  | 是否开启caffeine cache统计  15s一次                                                  |
| nodes       | Y  | 节点定义 com.codestepfish.vline.core.Node                                        |
| -name       | Y  | 节点名称 全局唯一                                                                    |
| -type       | Y  | 节点类型 com.codestepfish.vline.core.enums.NodeType                              |
| -extra      | N  | 节点扩展属性 key: value                                                            |
| -properties | Y  | com.codestepfish.vline.core.Node 节点属性Properties                              |
| struct      | Y  | 数据传输拓扑结构 key: 入口节点 value: 出口节点集合    Map<String, List<String>> k/v都是Node name |

### tcp 🛰️

> com.codestepfish.vline.core.tcp.TcpProperties

| key             | 必填 | desc                                                                      |
|-----------------|----|---------------------------------------------------------------------------|
| mode            | Y  | tcp节点类型: SERVER/CLIENT com.codestepfish.vline.core.tcp.TcpProperties.Mode |
| host            | Y  | client/server 客户端IP                                                       |
| port            | Y  | client/server 端口                                                          |
| reconnect-delay | N  | client节点有效 重连间隔时间                                                         |
| child-handler   | Y  | childHandler ChannelInitializer<SocketChannel>                            |                                                             |

### http 🛰️

上层http请求具体处理逻辑 实现 `com.codestepfish.vline.http.HttpHandler`, 并加入`Spring IOC`

> com.codestepfish.vline.core.http.HttpProperties

1. 目前http只支持作为 `out node`

| key     | 必填 | desc                                                    |
|:--------|----|---------------------------------------------------------|
| handler | Y  | 自定义http请求处理逻辑 实现com.codestepfish.vline.http.HttpHandler |

### redis 🛰️

1. 上层通过`com.codestepfish.vline.redis.RedisClientHolder`获取节点对应的`RedissonClient`
2. `redisson`配置文件位置: `classpath:redisson/{nodeName}.yml`
3. 上层应用应当排除`RedisAutoConfiguration`

> com.codestepfish.vline.core.redis.RedisProperties

| key         | 必填 | desc                                                      |
|:------------|----|-----------------------------------------------------------|
| mode        | N  | SINGLE, CLUSTER, SENTINEL, REPLICATED, MASTER_SLAVE       |
| dataHandler | Y  | 实现com.codestepfish.vline.redis.handler.RedisDataHandler接口 |

### mssql 🛰️

> com.codestepfish.vline.core.mssql.MssqlProperties

1. node节点(read/write mode)上层必须实现 `com.codestepfish.vline.mssql.handler.MssqlReadHandler/MssqlWriteHandler`
   接口
2. 模块依赖了 `spring-boot-starter-jdbc` , 如果不需要springboot自动配置数据源 , 上层应用应当排除
   `DataSourceAutoConfiguration`
3. flyway文件位置`classpath:sqlserver/{nodeName}`  (< 2008 版本不一定支持)

| key                    | 必填 | desc                                                                                   |
|:-----------------------|----|----------------------------------------------------------------------------------------|
| mode                   | N  | read/write/other(仅注入数据源)                                                               |
| host                   | N  | 默认127.0.0.1                                                                            |
| port                   | N  | 默认1433                                                                                 |
| databaseName           | Y  | 数据库                                                                                    |
| username               | Y  | 账号                                                                                     |
| password               | Y  | 密码                                                                                     |
| encrypt                | N  | 默认false                                                                                |
| trustServerCertificate | N  | 默认true                                                                                 |
| driverClassName        | N  | 默认com.microsoft.sqlserver.jdbc.SQLServerDriver(jtds默认net.sourceforge.jtds.jdbc.Driver) |
| jdbcUrl                | N  | 完整jdbc url                                                                             |
| dataHandler            | Y  | 数据处理具体实现 实现 com.codestepfish.vline.mssql.handler.MssqlReadHandler/MssqlWriteHandler 接口 |
| flyway                 | N  | 默认false                                                                                |

### mysql 🛰️

> com.codestepfish.vline.core.mysql.MysqlProperties

1. node节点(read/write mode)上层必须实现 `com.codestepfish.vline.mysql.handler.MysqlReadHandler/MysqlWriteHandler` 接口
2. 模块依赖了 `spring-boot-starter-jdbc` , 如果不需要springboot自动配置数据源 , 上层应用应当排除
   `DataSourceAutoConfiguration`
3. flyway文件位置`classpath:mysql/{nodeName}`

| key             | 必填 | desc                                                                                   |
|:----------------|----|----------------------------------------------------------------------------------------|
| mode            | N  | read/write/other(仅注入数据源)                                                               |
| host            | N  | 默认127.0.0.1                                                                            |
| port            | N  | 默认3306                                                                                 |
| databaseName    | Y  | 数据库                                                                                    |
| username        | Y  | 账号                                                                                     |
| password        | Y  | 密码                                                                                     |
| driverClassName | N  | 默认com.mysql.cj.jdbc.Driver                                                             |
| jdbcUrl         | N  | 完整jdbc url                                                                             |
| dataHandler     | Y  | 数据处理具体实现 实现 com.codestepfish.vline.mysql.handler.MysqlReadHandler/MysqlWriteHandler 接口 |
| flyway          | N  | 默认false                                                                                |

### postgresql 🛰️

> com.codestepfish.vline.core.postgres.PostgresProperties

1. node节点(read/write mode)上层必须实现
   `com.codestepfish.vline.postgres.handler.PostgresReadHandler/PostgresWriteHandler`
   接口
2. 模块依赖了 `spring-boot-starter-jdbc` , 如果不需要springboot自动配置数据源 , 上层应用应当排除
   `DataSourceAutoConfiguration`
3. flyway文件位置`classpath:postgres/{nodeName}`

| key             | 必填 | desc                                                                                            |
|:----------------|----|-------------------------------------------------------------------------------------------------|
| mode            | N  | read/write/other(仅注入数据源)                                                                        |
| host            | N  | 默认127.0.0.1                                                                                     |
| port            | N  | 默认5432                                                                                          |
| databaseName    | Y  | 数据库                                                                                             |
| username        | Y  | 账号                                                                                              |
| password        | Y  | 密码                                                                                              |
| driverClassName | N  | 默认org.postgresql.Driver                                                                         |
| jdbcUrl         | N  | 完整jdbc url                                                                                      |
| dataHandler     | Y  | 数据处理具体实现 实现 com.codestepfish.vline.postgres.handler.PostgresReadHandler/PostgresWriteHandler 接口 |
| flyway          | N  | 默认false                                                                                         |

### sqlite 🛰️

> com.codestepfish.vline.core.sqlite.SqliteProperties

1. node节点(read/write mode)上层必须实现 `com.codestepfish.vline.sqlite.handler.SqLiteReadHandler/SqLiteWriteHandler` 接口
2. 模块依赖了 `spring-boot-starter-jdbc` , 如果不需要springboot自动配置数据源 , 上层应用应当排除
   `DataSourceAutoConfiguration`
3. flyway文件位置`classpath:sqlite/{nodeName}`

| key             | 必填 | desc                                                                                      |
|:----------------|----|-------------------------------------------------------------------------------------------|
| mode            | N  | read/write/other(仅注入数据源)                                                                  |
| dbPath          | Y  | 数据库文件路径                                                                                   |
| driverClassName | N  | 默认org.sqlite.JDBC                                                                         |
| jdbcUrl         | N  | 完整jdbc url                                                                                |
| dataHandler     | Y  | 数据处理具体实现 实现 com.codestepfish.vline.sqlite.handler.SqLiteReadHandler/SqLiteWriteHandler 接口 |
| flyway          | N  | 默认false                                                                                   |

### oracle 🛰️

> com.codestepfish.vline.core.oracle.OracleProperties

1. node节点(read/write mode)上层必须实现
   `com.codestepfish.vline.oracle.handler.OracleReadHandler/OracleWriteHandler`
   接口
2. 模块依赖了 `spring-boot-starter-jdbc` , 如果不需要springboot自动配置数据源 , 上层应用应当排除
   `DataSourceAutoConfiguration`
3. flyway文件位置`classpath:oracle/{nodeName}`

| key             | 必填 | desc                                                                                      |
|:----------------|----|-------------------------------------------------------------------------------------------|
| mode            | N  | read/write/other(仅注入数据源)                                                                  |
| host            | N  | 默认127.0.0.1                                                                               |
| port            | N  | 默认1521                                                                                    |
| serviceName     | Y  | 服务名称                                                                                      |
| username        | Y  | 账号                                                                                        |
| password        | Y  | 密码                                                                                        |
| driverClassName | N  | 默认oracle.jdbc.driver.OracleDriver                                                         |
| jdbcUrl         | N  | 完整jdbc url  jdbc:oracle:thin:@host:port:serviceName                                       |
| dataHandler     | Y  | 数据处理具体实现 实现 com.codestepfish.vline.oracle.handler.OracleReadHandler/OracleWriteHandler 接口 |
| flyway          | N  | 默认false                                                                                   |

### etcd 🛰️

> com.codestepfish.vline.core.etcd.EtcdProperties

1. node节点上层必须实现 `com.codestepfish.vline.etcd.handler.EtcdDataHandler` 接口

| key         | 必填 | desc                                                               |
|:------------|----|--------------------------------------------------------------------|
| endpoints   | Y  | etcd endpoints 多个节点逗号分隔                                            |
| dataHandler | Y  | 数据处理具体实现 实现 com.codestepfish.vline.etcd.handler.EtcdDataHandler 接口 |

### h2 🛰️

> com.codestepfish.vline.core.h2.H2Properties

1. node节点(read/write mode)上层必须实现 `com.codestepfish.vline.h2.handler.H2ReadHandler/H2WriteHandler` 接口
2. 模块依赖了 `spring-boot-starter-jdbc` , 如果不需要springboot自动配置数据源 , 上层应用应当排除
   `DataSourceAutoConfiguration`
3. flyway文件位置`classpath:h2/{nodeName}`

| key             | 必填 | desc                                                                          |
|:----------------|----|-------------------------------------------------------------------------------|
| mode            | N  | read/write/other(仅注入数据源)                                                      |
| driverClassName | N  | 默认org.h2.Driver                                                               |
| jdbcUrl         | N  | 完整jdbc url                                                                    |
| dataHandler     | Y  | 数据处理具体实现 实现 com.codestepfish.vline.h2.handler.H2ReadHandler/H2WriteHandler 接口 |
| flyway          | N  | 默认false                                                                       |

### serial port 🛰️

> com.codestepfish.vline.core.serialport.SerialPortProperties

1. node节点上层必须实现 `com.codestepfish.vline.serialport.handler.SerialPortDataHandler` 接口

| key          | 必填 | desc                                                                           |
|:-------------|----|--------------------------------------------------------------------------------|
| device       | Y  | read/write                                                                     |
| baudRate     | N  | 串口波特率     默认115200                                                             |
| dataBits     | N  | 数据位         默认8                                                                |
| stopBits     | N  | 停止位         默认 1                                                               |
| parity       | N  | 奇偶校验        默认 0                                                               |
| useRs485Mode | N  | 是否使用rs485模式        默认 false                                                    |
| ignored      | N  | 是否忽略其它业务处理  只转发数据                                                              |
| dataHandler  | Y  | 数据处理具体实现 实现 com.codestepfish.vline.serialport.handler.SerialPortDataHandler 接口 |
