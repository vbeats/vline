# vline

多数据源 数据采集、传输中间件, 轻量级 边缘设备场景 ⚓

Not ETL

![alt text](/images/vline.svg)

## dependency

> JDK21 springboot3.4.1 其它版本未测试 repository尚未推送

```gradle
    implementation project("com.codestepfish.vline:vline-spring-boot-starter:${version}")
    
    // 对应数据源依赖
    implementation project("com.codestepfish.vline:vline-tcp:${version}")
    implementation project("com.codestepfish.vline:vline-http:${version}")
    .....
```

## ToDo

| module             | progress |
|--------------------|----------|
| tcp                | ✅        |
| http               | ☑️待完善    |
| redis              | ➖        |
| mysql              | ➖        |
| postgresql         | ➖        |
| sql-server-2008-r2 | ➖        |
| sql-server-2000    | ➖        |
| serial-port        | ➖        |
| 独立文档               | ⭕        |

## desc

1. ~~msg data持久化 保证数据传递不丢失~~  由上层应用层实现

## module

| module                    | desc                                           |
|---------------------------|------------------------------------------------|
| vline-core                | 定义Node基本信息、属性, Node可以理解为入口/出口对应的数据源            |
| vline-tcp                 | netty tcp                                      |
| vline-http                | forest 实现                                      |
| vline-spring-boot-starter | spring boot starter : yml解析 初始化  event bus事件监听 |
| examples                  | 示例                                             |

## yaml config

> com.codestepfish.vline.spring.boot.starter.VLineProperties

### basic config

```yaml
vline:
  nodes:
    - name: t1
      type: tcp
      tags:
        - xx
        - oo
      # protocol:
      #  - property:
      #  - property:
  struct:
    t1:
      - t2
```

| key         | 必填 | desc                                                                         |
|:------------|----|------------------------------------------------------------------------------|
| nodes       | Y  | 节点定义 com.codestepfish.vline.core.Node                                        |
| -name       | Y  | 节点名称 全局唯一                                                                    |
| -type       | Y  | 节点类型 com.codestepfish.vline.core.enums.NodeType                              |
| -tags       | N  | 节点标签                                                                         |
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

上层http请求具体处理逻辑 重写 `com.codestepfish.vline.http.client.ForestHandler.handle`方法

> com.codestepfish.vline.core.http.HttpProperties

1. 目前http只支持作为 `out node`
2. 数据 `data` 目前只支持 `json string`

| key                | 必填 | desc                                                  |
|:-------------------|----|-------------------------------------------------------|
| url                | Y  | 完整url                                                 |
| method             | N  | 默认 POST, com.codestepfish.vline.core.enums.HttpMethod |
| max-retry-count    | N  | 默认0 最大重试次数                                            |
| max-retry-interval | N  | 默认0 毫秒 最大重试间隔 ,maxRetryCount>0时有效                     |
| retry-when         | N  | 重试条件 实现 RetryWhen 接口                                  |
| success-when       | N  | 请求成功条件 实现 SuccessWhen 接口                              |
| on-error           | N  | 请求失败处理 实现 OnError 接口                                  |
| handler            | N  | 自定义http请求处理逻辑 重写ForestHandler.handle方法                |

### mssql 🛰️

> com.codestepfish.vline.core.mssql.MssqlProperties

1. node节点上层必须实现 `com.codestepfish.vline.mssql2008r2.handler.MssqlReadHandler/MssqlWriteHandler` 接口
2. 模块依赖了 `spring-boot-starter-jdbc` , 如果不需要springboot自动配置数据源 , 上层应用应当排除
   `DataSourceAutoConfiguration`

| key                    | 必填 | desc                                                                                         |
|:-----------------------|----|----------------------------------------------------------------------------------------------|
| mode                   | N  | read/write                                                                                   |
| host                   | N  | 默认127.0.0.1                                                                                  |
| port                   | N  | 默认1433                                                                                       |
| databaseName           | Y  | 数据库                                                                                          |
| username               | Y  | 账号                                                                                           |
| password               | Y  | 密码                                                                                           |
| encrypt                | N  | 默认false                                                                                      |
| trustServerCertificate | N  | 默认true                                                                                       |
| driverClassName        | N  | 默认com.microsoft.sqlserver.jdbc.SQLServerDriver                                               |
| jdbcUrl                | N  | 完整jdbc url                                                                                   |
| dataHandler            | Y  | 数据处理具体实现 实现 com.codestepfish.vline.mssql2008r2.handler.MssqlReadHandler/MssqlWriteHandler 接口 |
