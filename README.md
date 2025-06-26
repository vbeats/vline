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

## Support

| module             | remark | progress |
|--------------------|--------|----------|
| tcp                |        | ✅        |
| http               |        | ✅        |
| redis              |        | ➖        |
| mysql              |        | ✅        |
| sqlite             |        | ✅        |
| postgresql         |        | ➖        |
| sql-server-2008-r2 |        | ✅        |
| sql-server-2000    |        | ✅        |
| serial-port        |        | ✅        |

## desc

1. ~~msg data持久化 保证数据传递不丢失~~  由上层应用层实现

## module

`database` 数据库相关操作 基于`anyline`

| module                    | desc                                           |
|---------------------------|------------------------------------------------|
| vline-core                | 定义Node基本信息、属性, Node可以理解为入口/出口对应的数据源            |
| vline-tcp                 | netty tcp                                      |
| vline-http                | 自定义实现                                          |
| vline-mysql               | mysql8 其它未测试                                   |
| vline-sqlite              | sqlite                                         |
| vline-sql-server-2008-r2  | sql server2008 R2                              |
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

### mssql 🛰️

> com.codestepfish.vline.core.mssql.MssqlProperties

1. node节点(read/write mode)上层必须实现 `com.codestepfish.vline.mssql2008r2.handler.MssqlReadHandler/MssqlWriteHandler`
   接口
2. 模块依赖了 `spring-boot-starter-jdbc` , 如果不需要springboot自动配置数据源 , 上层应用应当排除
   `DataSourceAutoConfiguration`

| key                    | 必填 | desc                                                                                         |
|:-----------------------|----|----------------------------------------------------------------------------------------------|
| mode                   | N  | read/write/other(仅注入数据源)                                                                     |
| host                   | N  | 默认127.0.0.1                                                                                  |
| port                   | N  | 默认1433                                                                                       |
| databaseName           | Y  | 数据库                                                                                          |
| username               | Y  | 账号                                                                                           |
| password               | Y  | 密码                                                                                           |
| encrypt                | N  | 默认false                                                                                      |
| trustServerCertificate | N  | 默认true                                                                                       |
| driverClassName        | N  | 默认com.microsoft.sqlserver.jdbc.SQLServerDriver(2000默认net.sourceforge.jtds.jdbc.Driver)       |
| jdbcUrl                | N  | 完整jdbc url                                                                                   |
| dataHandler            | Y  | 数据处理具体实现 实现 com.codestepfish.vline.mssql2008r2.handler.MssqlReadHandler/MssqlWriteHandler 接口 |

### mysql 🛰️

> com.codestepfish.vline.core.mysql.MysqlProperties

1. node节点(read/write mode)上层必须实现 `com.codestepfish.vline.mysql.handler.MysqlReadHandler/MysqlWriteHandler` 接口
2. 模块依赖了 `spring-boot-starter-jdbc` , 如果不需要springboot自动配置数据源 , 上层应用应当排除
   `DataSourceAutoConfiguration`

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

### sqlite 🛰️

> com.codestepfish.vline.core.sqlite.SqliteProperties

1. node节点(read/write mode)上层必须实现 `com.codestepfish.vline.sqlite.handler.SqLiteReadHandler/SqLiteWriteHandler` 接口
2. 模块依赖了 `spring-boot-starter-jdbc` , 如果不需要springboot自动配置数据源 , 上层应用应当排除
   `DataSourceAutoConfiguration`

| key             | 必填 | desc                                                                                      |
|:----------------|----|-------------------------------------------------------------------------------------------|
| mode            | N  | read/write/other(仅注入数据源)                                                                  |
| dbPath          | Y  | 数据库文件路径                                                                                   |
| driverClassName | N  | 默认org.sqlite.JDBC                                                                         |
| jdbcUrl         | N  | 完整jdbc url                                                                                |
| dataHandler     | Y  | 数据处理具体实现 实现 com.codestepfish.vline.sqlite.handler.SqLiteReadHandler/SqLiteWriteHandler 接口 |

### serial port 🛰️

> com.codestepfish.vline.core.serialport.SerialPortProperties

1. node节点上层必须实现 `com.codestepfish.vline.serialport.handler.SerialPortDataHandler` 接口

| key         | 必填 | desc                                                                           |
|:------------|----|--------------------------------------------------------------------------------|
| device      | Y  | read/write                                                                     |
| ignored     | N  | 是否忽略其它业务处理  只转发数据                                                              |
| dataHandler | Y  | 数据处理具体实现 实现 com.codestepfish.vline.serialport.handler.SerialPortDataHandler 接口 |
