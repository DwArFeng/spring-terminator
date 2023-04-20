# ChangeLog

### Release_1.0.11_20230420_build_A

#### 功能构建

- 依赖升级。
  - 升级 `spring` 依赖版本为 `5.3.27` 以规避漏洞。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.10_20221118_build_A

#### 功能构建

- 依赖升级。
  - 升级 `slf4j` 依赖版本为 `1.7.5` 以规避漏洞。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.9_20220903_build_A

#### 功能构建

- 插件升级。
  - 升级 `maven-deploy-plugin` 插件版本为 `2.8.2`。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.8_20220606_build_A

#### 功能构建

- 依赖升级。
  - 升级 `junit` 依赖版本为 `4.13.2` 以规避漏洞。
  - 升级 `spring` 依赖版本为 `5.3.20` 以规避漏洞。
  - 升级 `log4j2` 依赖版本为 `2.17.2` 以规避漏洞。
  - 升级 `slf4j` 依赖版本为 `1.7.36` 以规避漏洞。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.7_20201021_build_A

#### 功能构建

- 改进 Terminator 的阻塞逻辑。
  - getExitCode 方法只有在 ApplicationContext 完全启动后才会解除阻塞。
  - 该改动使得程序只有在 ApplicationContext 完全加载后才会退出，避免了潜在的 bug。

#### Bug修复

- 修复了 ApplicationUtil 使用含有关闭程序的语句的 Consumer 启动程序时抛出异常的 bug。

#### 功能移除

- (无)

---

### Release_1.0.6_20200928_build_A

#### 功能构建

- 对 Terminator 的重启方法增加了警告类型的文档注释。
- 增加对处理后置延时的中断的处理，并在文档注释中增加其描述。

#### Bug修复

- 修复了 Terminator 在 post blocking 时 `getExitCode()` 方法提前返回退出代码的 bug。

#### 功能移除

- (无)

---

### Release_1.0.5_20200927_build_A

#### 功能构建

- 为 Terminator 增加重启功能。
- 更新 ApplicationUtil，使其支持重启功能。
- 为 ApplicationUtil 添加启动后用于后续初始化的消费者支持。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.4_20200713_build_A

#### 功能构建

- 去除项目中无用的坐标信息。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.3_20200404_build_A

#### 功能构建

- 添加方法com.dwarfeng.springterminator.sdk.util.ApplicationUtil.launch(java.lang.String...)

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.2_20200327_build_B

#### 功能构建

- 升级subgrade依赖至beta-0.3.2.b。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.2_20200315_build_A

#### 功能构建

- 使配置文件支持 placeholder。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.1_20200315_build_A

#### 功能构建

- (无)

#### Bug修复

- 修正 com.dwarfeng.springterminator.node.config 包下类的不合理的命名。

#### 功能移除

- (无)

---

### Release_1.0.0_20200314_build_A

#### 功能构建

- 基本功能实现。

#### Bug修复

- (无)

#### 功能移除

- (无)
