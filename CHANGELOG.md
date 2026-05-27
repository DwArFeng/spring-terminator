# ChangeLog

## Release_2.0.2_20260527_build_A

### 功能构建

- 依赖升级。
  - 升级 `spring-telqos` 依赖版本为 `2.0.2.a` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_2.0.1_20260527_build_A

### 功能构建

- Wiki 编写。
  - docs/wiki/zh-CN/UsageGuide.md。

- `spring-terminator-api` 子模块配置文件优化。
  - telqos/connection.properties。

- 依赖升级。
  - 升级 `subgrade` 依赖版本为 `1.8.3.a` 以规避漏洞。
  - 升级 `spring-telqos` 依赖版本为 `2.0.1.a` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_2.0.0_20260506_build_A

### 功能构建

- Wiki 编写。
  - docs/wiki/zh-CN/InstallBySourceCode.md。

- 更新 README.md。

- Wiki 更新。
  - docs/wiki/zh-CN/Introduction.md。

- 新增 spring-telqos 框架集成指令。
  - com.dwarfeng.springterminator.api.integration.springtelqos.ShutdownCommand。

- 优化 xsd 配置项解析机制。
  - 将更多的 xsd 配置常量定义在 `com.dwarfeng.springterminator.sdk.util.Constants` 中。
  - 调整 `META-INF/spring-terminator.xsd` 中的默认值，使用 SpEL 表达式引用 `Constants` 中的常量。
  - 在 `com.dwarfeng.springterminator.sdk.util.BeanDefinitionParserUtil` 中增加必要的工具方法。

- 优化 xsd 配置项名称。
  - 将 `terminator:config:config-id` 配置项更名为 `terminator:config:config-name`。
  - 将 `terminator:qos:handler-name` 配置项更名为 `terminator:qos:handler-ref`。

- 新增 QoS 服务。
  - com.dwarfeng.springterminator.stack.service.TerminateQosService。

- 增加示例。
  - com.dwarfeng.springterminator.node.example.Example。

- 重构项目模块。
  - 新增 `spring-terminator-core` 子模块，并迁移原有代码至该模块。
  - 新增 `spring-terminator-api` 子模块。

- 重构项目配置机制。
  - 新增 `com.dwarfeng.springterminator.stack.struct.TerminateConfig` 配置类。
  - `com.dwarfeng.springterminator.impl.handler.TerminateHandlerImpl` 切换为构造器注入。
  - 优化 `spring-terminator.xsd` 的命名空间模型。
  - 其余配套逻辑调整。

- 重构项目结构。
  - 与 subgrade 集成，处理器层与 subgrade 对齐。
  - 与 subgrade 集成，栈异常与 subgrade 对齐。

- 增加依赖。
  - 增加依赖 `spring-telqos` 以应用其新功能，版本为 `2.0.0.a`。
  - 增加依赖 `commons-lang3` 以应用其新功能，版本为 `3.18.0`。
  - 增加依赖 `dutil` 以应用其新功能，版本为 `0.4.2.a-beta`。
  - 增加依赖 `subgrade` 以应用其新功能，版本为 `1.8.2.a`。

- 优化文件格式。
  - 优化 `spring-telqos.xsd` 文件的格式。

### Bug 修复

- (无)

### 功能移除

- 删除无意义的单元测试。
  - com.dwarfeng.springterminator.impl.handler.TerminatorImplTest。

---

## 更早的版本

[View all changelogs](./changelogs)
