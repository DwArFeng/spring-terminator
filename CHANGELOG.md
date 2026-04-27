# ChangeLog

## Release_2.0.0_20260421_build_A

### 功能构建

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
  - 增加依赖 `dutil` 以应用其新功能，版本为 `0.4.2.a-beta`。
  - 增加依赖 `subgrade` 以应用其新功能，版本为 `1.8.2.a`。

- 优化文件格式。
  - 优化 `spring-telqos.xsd` 文件的格式。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## 更早的版本

[View all changelogs](./changelogs)
