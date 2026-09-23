# AGENTS.md

> 本文件是给 AI 编码助手的项目指南。修改代码前请先读完，尤其是[易踩的坑](#8-易踩的坑)。

## 1. 项目简介

**vline** —— 轻量级多数据源采集 / 传输中间件，面向边缘设备场景。`com.codestepfish` 组织，MIT 协议。

**设计定位：不是 ETL。** 它不做数据转换编排，只负责：把「入口节点(Node)」收到的数据，按 yaml 里声明的拓扑 `struct` 转发给「出口节点」，具体业务由使用方实现的 `*DataHandler` 完成。

一句话理解数据流：

```
数据源/tcp/串口/mqtt  ...
      │  VLineContext.pushMsg(nodeName, payload)
      ▼
Disruptor RingBuffer (1024)
      │  VLineEventHandler
      ▼  VLineContext.nextNodes(key)  ← struct 反查上下游
VLineContext.NODES.get(name).receiveData(payload)
      │
      ▼
用户实现的 XxxDataHandler.rec(node, data)
```

### 版本与分支（务必对齐，勿跨代改动）

| branch | JDK | Spring Boot | vline version | 状态 |
|---|---|---|---|---|
| `2.x` | 1.8 | 2.x | 3.x | 已废弃 |
| `3.x` | 21 | 3.x | 4.x | 已废弃 |
| **`main`** | **21+** | **4.x** | **5.x** | **当前开发** |

`CLAUDE.md` 仅含一行 `@AGENTS.md`，是 Claude Code 的引用入口，不要往里面写重复内容。

## 2. 技术栈

| 项 | 值 | 出处 |
|---|---|---|
| Java | 21（toolchain + source/target），开启了 `-parameters` 编译参数 | `build.gradle:31-50` |
| Spring Boot | `4.1.0`（BOM 管理） | `gradle.properties:10` |
| Gradle | 9.6.1（阿里云镜像） | `gradle/wrapper/gradle-wrapper.properties` |
| 当前版本 | `5.2.3` | `gradle.properties:8` |
| ORM | **AnyLine**（数据库操作统一走它，不是 JPA / MyBatis） | `version.gradle` → `libs.get('anyline-*')` |
| 连接池 | Druid | JDBC 系模块的 `DataSourceInitializer` |
| 事件总线 | LMAX Disruptor + **虚拟线程** | `DisruptorConfig` |
| 缓存 | Caffeine（`vlineCacheManager`） | `VLineCacheConfig` |
| 工具库 | Hutool（Bean 拷贝、SpringUtil）、fastjson2、Guava | 各模块 |

> `-parameters` 已开启，Spring 的参数名绑定依赖它，不要去掉。

## 3. 构建与常用命令

Windows 用 `gradlew.bat`，Linux/macOS 用 `./gradlew`。

```bash
# 编译全部（含 examples）
./gradlew build

# 只编译某个模块（跳过测试，推荐日常使用）
./gradlew :vline-tcp:build -x test

# 编译某个 example
./gradlew :examples:vline-ex-tcp:build -x test

# 清理
./gradlew clean
```

发布（**不要随意执行**，会推 Maven Central）：`publish.sh` = `clean` → `publish` → `jreleaserFullRelease`。

**注意**：
- `settings.gradle:29-41` 会自动扫描并 include `examples/` 下**所有**子目录，新建示例不用手动改 settings。
- 项目**没有** CI、git hooks、checkstyle、spotless、`.editorconfig`。没有格式化门禁，风格一致性靠人工维持 —— 新增代码请严格对齐周围代码的写法。

## 4. 目录结构

```
vline-core                 节点模型 / Properties / 事件对象 / DataHandler 接口（不依赖任何驱动）
vline-{protocol}x15        各协议实现：XxxNode + XxxNodeAutoConfiguration + Holder + handler
vline-spring-boot-starter  自动装配 + 事件总线 + 缓存 + 生命周期
examples/*                 6 个可运行示例（自动纳入构建）
build.gradle               根构建：Maven 发布 + JReleaser 签名
version.gradle             versions / libs 两张坐标表，改依赖版本只动这里
docs/                      架构图（png / html / json）
```

### 模块名 ↔ 包名不一致（易混淆）

| Gradle 模块名 | Java 包名 |
|---|---|
| `vline-sql-server-mssql` | `com.codestepfish.vline.mssql` |
| `vline-sql-server-jtds` | `com.codestepfish.vline.jtds` |
| `vline-serial-port` | `com.codestepfish.vline.serialport` |

## 5. 核心 API（vline-core）

### `com.codestepfish.vline.core.Node`

节点模型，`implements INode, Serializable`。字段：`NodeType type`、`String name`（**全局唯一**）、`Map<String,Object> extra`（业务自定义）、以及 15 个协议属性（一个 node 只配一种）。

**关键机制**：每个协议 setter 会顺带把 `type` 置为对应枚举。

```java
public void setMysql(MysqlProperties mysql) {
    this.mysql = mysql;
    if (!ObjectUtils.isEmpty(mysql)) {
        this.type = NodeType.MYSQL;
    }
}
```

→ **结论：yaml 里写 `- mysql: {...}` 就不必再写 `type: mysql`。** 反过来若显式写了 `type`，它会被后面的属性 setter 覆盖或保持，二者保持一致即可。

### `com.codestepfish.vline.core.INode`

```java
void init();
void destroy();
void receiveData(Object data);
```

### `com.codestepfish.vline.core.VLineProperties`

`@ConfigurationProperties(prefix = "vline")`，三个字段：

| 字段 | 说明 |
|---|---|
| `List<Node> nodes` | 节点定义 |
| `Map<String, List<String>> struct` | 拓扑：key=入口节点名，value=出口节点名列表 |
| `Boolean cacheStats = false` | 是否按 60s 打印 Caffeine 统计 |

### `com.codestepfish.vline.core.VLineContext`

运行时上下文，**日常使用只接触两个成员**：

- `VLineContext.NODES`：`Map<String, Node>`，node name → Node 实例。
- `VLineContext.pushMsg(String nodeName, Object payload)`：把数据投递到事件总线。`nodeName` 必须是 yaml 中已声明且存在于 `struct` 里的名字，否则数据无人接收。

`nextNodes(String)` 由 `struct` 双向反查上下游，`@Cacheable(cacheManager = "vlineCacheManager")`。

### `com.codestepfish.vline.core.handler.DataHandler<N extends Node>`

所有 handler 的统一签名：

```java
void init(N node);
void rec(N node, Object data);   // 收到数据 → 业务写这里
void destroy(N node);
```

**例外**：`SerialPortDataHandler` 不继承它，有自己的三方法（`receive` / `send` / `destroy`）。

### Properties 位置

**所有协议 Properties 都在 `vline-core`**（包 `com.codestepfish.vline.core.xxx`），与各实现模块解耦，因此 core 不含任何 JDBC 驱动：

`tcp.TcpProperties` / `http.HttpProperties`(空标记) / `redis.RedisProperties` / `mysql.MysqlProperties` / `postgres.PostgresProperties` / `mssql.MssqlProperties`(mssql 与 mssqlJtds 共用) / `oracle.OracleProperties` / `sqlite.SqliteProperties` / `h2.H2Properties` / `mongo.MongoProperties` / `duckdb.DuckProperties` / `etcd.EtcdProperties` / `mqtt.MqttProperties` / `serialport.SerialPortProperties`

各字段的默认值与必填性见 `README.md`，本文件不重复。

## 6. 使用方视角（写示例/改业务时看这里）

### yaml

```yaml
vline:
  cache-stats: false
  nodes:
    - name: t1
      type: tcp
      tcp:
        mode: server
        host: 0.0.0.0
        port: 9999
        child-handler: com.codestepfish.vlineex.server.T1ServerChannelHandler
    - name: db
      mysql:              # 不写 type 也行，setter 会推断
        host: 127.0.0.1
        database-name: test
        username: root
        password: root
  struct:
    t1:                   # key = 入口节点名
      - db                # value = 出口节点名列表
```

### 数据进入总线

在 Netty handler / 串口回调里：

```java
public void channelRead(ChannelHandlerContext ctx, Object msg) {
    VLineContext.pushMsg("t1", msg);   // "t1" 必须与 yaml 的 node name 一致
}
```

### 实现出口 handler

```java
@Slf4j
@Service
public class DbDataHandler implements MysqlDataHandler {
    @Override public void init(MysqlNode node) {}
    @Override public void rec(MysqlNode node, Object data) { /* 落库 */ }
    @Override public void destroy(MysqlNode node) {}
}
```

### 上层应用常见排除项

引入 JDBC 系模块时，如果不希望 Spring Boot 自动配数据源：

```java
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
```

引入 redis 时排除 `RedisAutoConfiguration`，引入 mongo 时额外排除 `MongoAutoConfiguration`。

## 7. 开发规约：新增一个协议模块

所有 15 个协议模块结构**完全同构**，照抄最近的那个（建议抄 `vline-mqtt` 或 `vline-etcd`）即可。

### 目录骨架

```
vline-xxx/
  XxxNode                     extends Node，覆写 init/destroy/receiveData
  XxxNodeAutoConfiguration    @Configuration，过滤 NodeType → copy → init → 注册 NODES
  XxxClientHolder             （有客户端连接时）静态 Map<String, Client>
  handler/XxxDataHandler      extends DataHandler<XxxNode>
  DataSourceInitializer       （JDBC 系）Druid + AnyLine reg + Flyway
  src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

### Checklist

1. `vline-core` 新增 `core/xxx/XxxProperties`
2. `Node` 增加字段 + setter（**setter 里顺带设 `type`**）
3. `core/enums/NodeType` 增加枚举
4. 新建模块：`settings.gradle` include、`build.gradle` 依赖（尽量只用 `api(project(":vline-core"))` + 需要的驱动）
5. 版本坐标写进 `version.gradle` 的 `versions` / `libs`，**不要在子模块里硬编码版本号**
6. 按上面的骨架写 4 个类 + 注册 `AutoConfiguration.imports`
7. `README.md` 的 Support 表和 module 表同步更新
8. 需要时在 `examples/` 加一个示例（看情况，非必须）

### AutoConfiguration 三段式模板

```java
@ConditionalOnClass(XxxNode.class)
@EnableConfigurationProperties({VLineProperties.class})
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline")
public class XxxNodeAutoConfiguration {
    @PostConstruct
    public void xxxNodeInit() {
        List<Node> nodes = vLineProperties.getNodes().stream()
                .filter(e -> NodeType.XXX.equals(e.getType())).toList();
        nodes.forEach(node -> {
            XxxNode xxxNode = BeanUtil.copyProperties(node, XxxNode.class);
            xxxNode.init();
            VLineContext.NODES.put(node.getName(), xxxNode);
        });
    }
}
```

**触发时机二选一**，遵照现有模块的习惯：TCP / HTTP / 串口 用 `@PostConstruct`（立即）；Redis / MySQL 等依赖其他 Bean 就绪的用 `implements ApplicationListener<ApplicationReadyEvent>`。

### XxxNode 惯例

```java
public void init() {
    super.init();                                              // 必须第一行
    this.xxxDataHandler = SpringUtil.getBean(XxxDataHandler.class);
    // 建立连接 / 数据源，try-catch 包一层 throw new RuntimeException(e)
}

@Override
public void receiveData(Object data) {
    this.xxxDataHandler.rec(this, data);                       // 统一委派
}

@Override
public void destroy() {
    super.destroy();
    this.xxxDataHandler.destroy(this);
    // 关闭客户端 / DataSourceHolder.destroy(name)
}
```

## 8. 易踩的坑

1. **`node.name` 必须全局唯一**。它是 `VLineContext.NODES`、`struct` 的 key、所有 Holder Map 的 key，还是 AnyLine 数据源 key（`ServiceProxy.service("<nodeName>")` 靠它取服务）。
2. **handler 必须是唯一的 Spring Bean**。Node 通过 `SpringUtil.getBean(XxxDataHandler.class)` 按类型获取，同类型多个实现会启动报错。
3. **两个 `MssqlDataHandler` 同名不同包**：`com.codestepfish.vline.mssql.handler.MssqlDataHandler` 和 `com.codestepfish.vline.jtds.handler.MssqlDataHandler`。同时引入两个 sqlserver 模块时必须用全限定名区分（参考 `examples/vline-ex-jtds`）。
4. **Redis 连接配置不在 `application.yml`**，必须在 `classpath:redis/<nodeName>.yml`（Redisson 格式），由 `Config.fromYAML` 加载。`application.yml` 里的 `redis.mode` 只用于日志。
5. **`init()` 必须先 `super.init()`**，`destroy()` 同理。
6. **命名不一致的历史遗留**：oracle 的初始化类叫 `OracleDataSourceInitializer`（其它叫 `DataSourceInitializer`）；sqlite 的类名是 `SqLiteNode` / `SqLiteDataHandler`（大写 L）；jtds 的自动配置类叫 `MssqlJtdsNodeAutoConfiguration`。
7. **Jackson 双版本并存**：`tools.jackson.*`（v3 新坐标）+ `com.fasterxml.jackson.core:jackson-annotations`（v2 旧坐标），`version.gradle` 里别合并这两组。
8. **HTTP 节点目前只能作为 out node**，`HttpProperties` 是空标记类。
9. **MQTT 只支持 v5**，其 handler 同时要 implement Paho `MqttCallback` 的全部 6 个回调。
10. `#include examples` 是全量扫描：在 `examples/` 下建一个临时目录也会被纳入构建，跑 `./gradlew build` 前确认它能编译。

## 9. 代码风格

照着现有代码写，要点：

- **Lombok 优先**：`@Getter @Setter @Slf4j @Accessors(chain = true)`，`@RequiredArgsConstructor` 注入，不手写 getter/setter、不手写 `private static final Logger`。
- 中文注释，行尾注释与控制流空格对齐的写法（`private NodeType type;` // 注释）保持现有风格。
- 日志用 `@Slf4j` 的 `log.xxx`，占位符 `{}`，**不要字符串拼接**。
- 不要引入新工具库，优先 Hutool / Guava / Apache Commons（已在依赖里）。
- 静态客户端注册表统一命名 `public static final Map<String, X> XXX_CLIENTS`，key 恒为 node name。
- 不改不动的抽象：**不要在没有充分理由时重构 `Node` 的 setter 推断 `type` 机制**，它是 yaml 松散绑定的基础。

## 10. 全局静态注册表速查

| Holder | Map | 位置 |
|---|---|---|
| `VLineContext.NODES` | `Map<String, Node>` | vline-core |
| `TcpHolder.CHANNEL_FUTURES` / `CLIENT_CHANNELS` | ChannelFuture / Channel | vline-tcp |
| `RedisClientHolder.REDIS_CLIENTS` | RedissonClient | vline-redis |
| `EtcdClientHolder.ETCD_CLIENTS` | io.etcd.jetcd.Client | vline-etcd |
| `MqttClientHolder.MQTT_CLIENTS` | MqttClient | vline-mqtt |
| `MongoClientHolder.MONGO_CLIENTS` | MongoClient | vline-mongo |
| `DuckClientHolder.DUCK_CLIENTS` | DuckDBConnection | vline-duckdb |
| `SerialPortHandler.SERIAL_PORTS` | SerialPort | vline-serial-port |
| `SerialPortNode.RATE_LIMITERS` | RateLimiter | vline-serial-port |

## 11. 参考资源

- 每种协议的 yaml 字段与默认值：`README.md`
- 可运行示例：`examples/vline-ex-tcp`（TCP 收发完整链路）、`vline-ex-mysql`（落库最小例）、`vline-ex-sp`（串口读）、`vline-ex-sp2`（串口→HTTP）、`vline-ex-jtds`（多 JDBC 源 + Forest 推送）、`vline-ex-mqtt`（MQTT v5）
- 架构图：`docs/vline-architecture.png`
- 上游仓库：<https://github.com/vbeats/vline>
