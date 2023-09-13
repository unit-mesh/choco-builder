# 后端代码规范

## 后端风格

建议采用社区上比较流行的《阿里巴巴 Java 开发手册》，安装相应的插件，可以结合 ArchUnit 来做命名上的约束。

1. 阿里巴巴 Java 开发手册：[GitHub](https://github.com/alibaba/p3c)
2. IntelliJ IDEA plugin 插件：[Idea Plugin](https://github.com/alibaba/p3c/tree/master/idea-plugin)
3. Eclipse 插件：[Eclipse Plugin](https://github.com/alibaba/p3c/tree/master/eclipse-plugin)

| 规范项       | 已有工具支持   | 额外编码 | 备注                                             |
|-----------|----------|------|------------------------------------------------|
| Java 编码规范 | 有        | 不需要  | 阿里 Java 规范                                     |
| 分层规范      | ArchUnit | 需要   | DDD 示例                                         |
| 代码工程架构规范  | 没有       | 需要   | 可以考虑微服务模板、类似于 COLA 等（含 CI、CD、k8s、SonarCloud 等） |
| 工程命名规范    | 没有       | 需要   | CI/CD、爬虫等                                      |

## 命名规范

统一的命名规范可以有效减少开发之间的沟通成本以及学习成本，更有利于项目的长期发展。

### 代码仓库

- 代码仓库命名：
- 代码工程名由小写英文字母、数字和 “-”（中横线） 组成。
- 业务实现类工程命名：系统-服务化中心-微服务。
- 公共实现类工程命名：系统-服务化中心-微服务-common。

### 微服务粒度 - DDD 分层（建议）命名

TODO：修改`展现层`改为 se 的名称。

展现层，应用层，领域层和基础设层的名字应分别为 `interface`、`application`、`domain`、`infrastructure`。

```bash
├── application
├── domain
├── infrastructure
└── interface
```

各层内部应按照聚合分包，包名就是聚合名字，通用部分放入 `common` 包。

1. 展现层对象（interface）：

- 通过各种协议对外暴露的接口，该层内包含 Request/Response 对象定义（强制定义此对象，避免直接对外暴露领域对象），命名使用 Request/Response 后缀，其可携带 Application 层定义的 DTO
  对象。暂不对 Request/Response 外的其它 DTO 对象定义形式有要求，推荐可使用inner class或定义DTO class。
- 基于异常数据尽早拒绝服务的原则，该层包含数据格式校验逻辑，
- 该层包含Converter对象定义，负责将Request/Response对象转换为应用层定义的DTO对象，装入应用层定义的命令中，应用层定义的 ApplicationService 为入口操作。

2. 应用层对象：

- 用于入参/出参的 DTO 类型定义，命名使用 DTO 后缀；
- 用于携带 DTO 数据的命令类型定义；
- 用于异常返回的异常类型定义；
- ApplicationService 类型定义，用于接收并处理命令；在 ApplicationService 中可使用 Domain 层定义的的 Entity、DomainService、Repository
  上的方法；Transaction 处理；发领域事件；

3. 领域层对象（Domain 示例：Hierarchy、Asset）：

- 聚合，实体，值对象类型定义，命名不使用后缀；
- 用于依赖反转的 Repository、Client 接口类型定义；
- DomainEvent 和其携带的DTO数据类型定义在共享包中，在领域层完成 DomainEvent 的产生；
- 领域层可定义 Domain Exception；

4. 基础设施层对象：

- 领域层定义的 Repository 的实现类 RepositoryImpl，其内部包括 JpaRepository；
- 对领域层定义的 Client 的实现类 ClientImpl，负责加载外部接口数据。
- 消息发送实现类；如 Kafka、RabbitMQ 等。
- Converter 类，用于将 DB，**第三方数据契约**等数据对象转换为领域层对象。
- 数据库实体对象定义，命名使用 PO（Persist Object）后缀。

示例项目：

- [https://github.com/domain-driven-design/ddd-monolithic-code-sample](https://github.com/domain-driven-design/ddd-monolithic-code-sample)

针对 RPC DDD 分层和 domain 层复用示例：

- [https://github.com/domain-driven-design/ddd-lite-example](https://github.com/domain-driven-design/ddd-lite-example)

### 代码命名

在遵循开发手册的基础上，参考如下的规范。

- 代码中的命名均不能以下划线或美元符号开始，也不能以下划线或美元符号结束。
- 代码中的命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式，正确的英文拼写和语法可以让阅读者易于理解，避免歧义。
- 类名使用 UpperCamelCase 风格，必须遵从驼峰形式。正例: HelloWorld。
- 方法名、参数名、成员变量、局部变量都统一使用 lowerCamelCase 风格，必须遵从驼峰形式。 正例: `getHttpMessage()`。
- 常量命名全部大写，单词间用下划线隔开，力求语义表达完整清楚，不要嫌名字长。 正例：`MAX_STOCK_COUNT` 反例： `MAX_COUNT`。
- 抽象类命名使用 Abstract或Base开头，异常类命名使用 Exception 结尾，测试类命名以它要测试的类的名称开始，以 Test 结尾。
- 包名统一使用小写，点分隔符之间有且仅有一个自然语义的英语单词。包名统一使用单数形式，但是类名如果有复数含义，类名可以使用复数形 式。 正例: 应用工具类包名为 `com.modernizing.*.util`
  、类名为 `MessageUtils` (此规则参考 spring 的框架结构)。
- 杜绝完全不规范的缩写，避免望文不知义。 反例: `AbstractClass` “缩写”命名成 `AbsClass`；`condition` “缩写” 命名成 `condi`，此类随意缩写严重降低了代码的可阅读性。

## 代码规范

### 统一请求对象命名

- 对于 HTTP API 接口来说，所有的请求应该遵循相同的命名规范，比如统一后缀 “DTO” 或者 “Command” 等;
- 请求对象应该保持与业务用 例的用语一致，比如对于“更新产品名称”的业务用例，对应的请求对象可以为 “UpdateProductNameCommand”。

### 统一返回对象命名

所有请求返回对象应该遵循相同的命名规范，比如统一后缀 “Response” 或者 “Representation” 等，比如对于“或者产品细节”的接口，其返回对象可以命名为“ProductDetailRepresentation”。

### 统一请求处理流程

对请求的处理流程应该在全团队中保持一致，并且保证清晰明 了。比如，在基于DDD的编码实践中，请求的处理流程大致为Controller -> ApplicationService -> DomainService ->
AggregateRoot -> Repository。

### 统一读写模型策略

软件中的读模型和写模型是很不一样的，通常情况下，开发团 队将读模型和写模型揉在一起，这并不是一种好的实践。因此，建议开发团队采用一套统一的读写模型策略。比如，可以采用 “统一数据库模型，分离代码读写模型“
策略，也可以采用“分离数据模型”策略。

## 日志规范

微服务的分布式系统架构风格为我们带来了很多益处，同时对开发运维人员在故障定位方面也带来了很大的挑战。适当的系统日志是必不可少的，有效的定位问题的手段。以下规范仅针对系统日志进行描述。

- 服务应该提供统一的日志格式，其中应该包含日志级别、时间戳、追踪ID、日志位置、日志信息等数据，例如:

```
2019-08-07 11:04:16.5190- f76417b1c5c2469289d3e83d74d811b5- INFO               
[http-nio-8083-exec-3] c.e.inventory.about.AboutController : Accessed about api
```

- 提供集中式日志系统，比如ELK等，以便在多节点部署场景下可以获取全量日志。 日志记录应该充足，在程序运行的重要环节均需要进行日志记录。 日志记录中不应该包含敏感信息，比如用户密码等。
- 日志级别使用场景为:
    - INFO:正常情况下的业务日志;
    - DEBUG:用于记录全面的调试信息，帮助开发人员进行复杂问题定位;
    - WARN:程序发生了异常，但是并不影响程序的执行;
    - ERROR:程序发生异常，比如终止相应请求的处理。
- 必须避免在循环体中记录非关键信息，如有必要可在退出循环处记录日志。如果必须在循环内记录关键信息，尽量将信息整合后再输出。
- INFO及INFO以上级别的日志，只记录结果，禁止记录过程。
- 禁止在INFO及INFO以上级别打印详细数据信息
- 对系统中可能短时间内频繁重复输出相同详细的日志点，可采用如下方法：
    1. 对相同日志做累计，在日志中记录单位时间里信息重复数和上下文信息
    2. 只记录信息状态变化（从成功到失败，从中断到恢复等）

## 异常规范

有效的异常返回，不但能够让客户端进行合理的处理，给用户更好的使用体验，还是开发运维人员进行问题定位的第一手信息，有必要进行统一处理。

- 对于异常情况，服务应该返回统一的异常数据格式，例如:

```json
{
  "requestId": "d008ef46bb4f4cf19c9081ad50df33bd",
  "error": {
    "code": "ORDER_NOT_FOUND",
    "status": 404,
    "message": "没有找到订单",
    "path": "/order",
    "timestamp": 1555031270087,
    "data": {
      "orderId": "123456789"
    }
  }
}
```

- 异常信息中应该包含请求的唯一ID(requestId)以便进行分布式追踪。
- 除了HTTP的错误状态码，异常信息中应该包含服务自定义的错误代码(error.code)，以便更准确第描述业务异常。
- 异常应该包含错误信息、时间戳以及出错地址等信息。
- 异常信息中不应该包含敏感数据，比如金额、密码和访问凭证(token)等。
- 异常不要用来做流程控制，条件控制，因为异常的处理效率比条件分支低。
- 捕获异常是为了处理它，不要捕获了却什么都不处理而抛弃之，如果不想处理它，请将该异常抛给它的调用者。最外层的业务使用者，必须处理 异常，将其转化为用户可以理解的内容。
- 防止NPE，是程序员的基本修养。
- 定义时区分unchecked / checked 异常，避免直接使用RuntimeException抛出，更不允许抛出Exception或者Throwable，应使用有业务含义 的自定义异常。
- 捕获异常与抛异常，必须是完全匹配，或者捕获异常是抛异常的父类。

## 文档规范

软件开发不是一蹴而就的一锤子买卖，而是一个长期的，人员变动较大的团队活动，信息、知识的承载不能仅仅依赖当前的人员，需要有更合理处理方式。本规范旨在帮助开发运维人员及第三方使用者减少不必要的沟通，而快速获取所需信息。

- 所有对外提供 API 的服务都必须对外提供 API 文档作为服务契约。
- API 文档必须保证与代码接口的实时一致性，可以使用诸如 Swagger 以自动更新 API 文档。
- API 文档必须包含调用地址、认证方式、请求格式、返回格式以及示例等内容。
- 每个微服务必须包含描述自身信息的文档，在 `README.md` 应包含如下内容：微服务的描述，架构图，负责人员信息，重要的信息链接。
- 每个微服务应包含开发上手指南，运行手册以及常见的问题答疑。

## 安全规范

- 代码及配置中不能出现明文的敏感数据，如 password，token 等
- 防止没有做水平权限校验就可随意访问、修改、删除别人的数据，比如查看他人的私信内容、修改他人的订单。隶属于用户个人的页面或者功能必须进行权限控制校验。
- 用户敏感数据禁止直接展示，必须对展示数据进行脱敏。如，中国大陆个人手机号码显示为: 137****0969，隐藏中间 4 位，防止隐私泄露。
- 用户输入的 SQL 参数严格使用参数绑定或者 METADATA 字段值限定，防止 SQL 注入，禁止字符串拼接 SQL 访问数据库。
- 用户请求传入的任何参数必须做有效性验证。如，忽略参数校验可能导致：过大 page size 导致内存溢出，恶意 order by 导致数据库慢查询，SQL 注入等问题。
- 禁止向 HTML 页面输出未经安全过滤或未正确转义的用户数据。
- 表单、AJAX 提交必须执行 CSRF 安全验证。
- 在使用平台资源，譬如短信、邮件、电话、下单、支付，必须实现正确的防重放的机制，如数量限制、疲劳度控制、验证码校验，避免被滥刷而导致资损。

