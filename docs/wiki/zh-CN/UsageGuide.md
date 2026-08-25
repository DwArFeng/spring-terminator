# Usage Guide - 使用指南

## 综述

本使用指南旨在帮助开发者快速上手 spring-terminator 框架，掌握如何配置和使用该框架构建自己的优雅停机能力。

spring-terminator 是一款基于 Spring 框架的程序终止器，它提供了：

- 通过配置快速搭建终止服务。
- 通过 `TerminateHandler` 在业务代码中触发退出或退出并重启。
- 通过 `TerminateQosService` 以服务层语义封装终止能力，便于与 subgrade 体系集成。
- 通过 `spring-terminator-api` 与 spring-telqos 集成，提供 `shutdown` 运维指令。

本指南将详细介绍如何配置框架、调用终止能力，以及框架的高级用法和最佳实践。

## 快速开始

### 添加依赖

首先，在项目的 `pom.xml` 文件中添加 spring-terminator 的核心依赖：

```xml
<?xml version="1.0" encoding="UTF-8"?>

<!--suppress MavenModelInspection, MavenModelVersionMissed -->
<project
        xmlns="http://maven.apache.org/POM/4.0.0"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
        http://maven.apache.org/xsd/maven-4.0.0.xsd"
>

    <!-- 省略其他配置 -->
    <dependencies>
        <!-- 省略其他配置 -->
        <dependency>
            <groupId>com.dwarfeng</groupId>
            <artifactId>spring-terminator-core</artifactId>
            <version>${spring-terminator.version}</version>
        </dependency>
        <!-- 省略其他配置 -->
    </dependencies>
    <!-- 省略其他配置 -->
</project>
```

如果需要使用框架提供的集成能力（如 spring-telqos `shutdown` 指令），还需要添加：

```xml
<?xml version="1.0" encoding="UTF-8"?>

<!--suppress MavenModelInspection, MavenModelVersionMissed -->
<project
        xmlns="http://maven.apache.org/POM/4.0.0"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
        http://maven.apache.org/xsd/maven-4.0.0.xsd"
>

    <!-- 省略其他配置 -->
    <dependencies>
        <!-- 省略其他配置 -->
        <dependency>
            <groupId>com.dwarfeng</groupId>
            <artifactId>spring-terminator-api</artifactId>
            <version>${spring-terminator.version}</version>
        </dependency>
        <!-- 省略其他配置 -->
    </dependencies>
    <!-- 省略其他配置 -->
</project>
```

### 基本配置

在 Spring 配置文件中添加 terminator 配置。以下是一个最小化配置示例：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:terminator="http://dwarfeng.com/schema/spring-terminator"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/spring-terminator
        http://dwarfeng.com/schema/spring-terminator/spring-terminator.xsd"
>

    <terminator:config/>
    <terminator:handler/>
</beans>
```

推荐使用与项目示例一致的平铺式写法：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:terminator="http://dwarfeng.com/schema/spring-terminator"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/spring-terminator
        http://dwarfeng.com/schema/spring-terminator/spring-terminator.xsd"
>

    <terminator:config pre-delay="1000" post-delay="500"/>
    <terminator:handler/>
</beans>
```

### 创建第一个终止入口

创建一个简单的业务类，注入 `TerminateHandler` 并暴露退出能力：

```java
package com.example.terminator;

import com.dwarfeng.springterminator.stack.handler.TerminateHandler;
import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProgramKiller {

    @Autowired
    private TerminateHandler terminateHandler;

    public void exit() throws HandlerException {
        terminateHandler.exit();
    }

    public void exitWithCode(int exitCode) throws HandlerException {
        terminateHandler.exit(exitCode);
    }

    public void exitAndRestart() throws HandlerException {
        terminateHandler.exitAndRestart();
    }

    public void exitAndRestartWithCode(int exitCode) throws HandlerException {
        terminateHandler.exitAndRestart(exitCode);
    }
}
```

### 启动和测试

使用 `ApplicationUtil.launch` 启动程序：

```java
package com.example.terminator;

import com.dwarfeng.springterminator.sdk.util.ApplicationUtil;

public class Main {

    static void main(String[] args) {
        ApplicationUtil.launch("classpath:spring/application-context*.xml");
    }
}
```

如果需要与 spring-telqos 联调，可以直接运行示例类：

```java
package com.dwarfeng.springterminator.api.integration.example;

import com.dwarfeng.springterminator.stack.handler.TerminateHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TelqosExample {

    static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:spring/application-context*.xml"
        );
        ctx.registerShutdownHook();
        ctx.start();

        TerminateHandler terminateHandler = ctx.getBean(TerminateHandler.class);

        System.out.println("开发者您好!");
        System.out.println("这是一个示例, 用于演示 dwarfeng-spring-terminator 的功能");
        System.out.println("您可以通过使用 telnet 客户端工具访问本机 ${telqos.port} 端口来体验本示例的功能");
        System.out.println("您可以通过使用 telqos 的 shutdown 命令来关闭本示例...");

        int exitCode = terminateHandler.getExitCode();
        boolean restartFlag = terminateHandler.getRestartFlag();

        System.out.println("退出代码: " + exitCode);
        System.out.println("重启标记: " + restartFlag);

        ctx.stop();
        ctx.close();
        System.exit(exitCode);
    }
}
```

## 配置详解

### config 配置

`terminator:config` 元素用于配置 `TerminateConfig`。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:terminator="http://dwarfeng.com/schema/spring-terminator"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/spring-terminator
        http://dwarfeng.com/schema/spring-terminator/spring-terminator.xsd"
>

    <terminator:config
            config-name="${terminator.config_name}"
            pre-delay="${terminator.pre_delay}"
            post-delay="${terminator.post_delay}"
    />
</beans>
```

#### 配置名称

- **属性名**：`config-name`。
- **类型**：`String`。
- **默认值**：`terminateConfig`。
- **说明**：`TerminateConfig` 的 bean 名称。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:terminator="http://dwarfeng.com/schema/spring-terminator"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/spring-terminator
        http://dwarfeng.com/schema/spring-terminator/spring-terminator.xsd"
>

    <terminator:config config-name="myTerminateConfig"/>
</beans>
```

#### 前置延时

- **属性名**：`pre-delay`。
- **类型**：`Long`。
- **默认值**：`-1`。
- **说明**：退出动作前的等待时长（毫秒）。小于等于 `0` 时表示不启用。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:terminator="http://dwarfeng.com/schema/spring-terminator"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/spring-terminator
        http://dwarfeng.com/schema/spring-terminator/spring-terminator.xsd"
>

    <terminator:config pre-delay="1000"/>
</beans>
```

#### 后置延时

- **属性名**：`post-delay`。
- **类型**：`Long`。
- **默认值**：`-1`。
- **说明**：退出动作后的等待时长（毫秒）。小于等于 `0` 时表示不启用。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:terminator="http://dwarfeng.com/schema/spring-terminator"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/spring-terminator
        http://dwarfeng.com/schema/spring-terminator/spring-terminator.xsd"
>

    <terminator:config post-delay="500"/>
</beans>
```

### handler 配置

`terminator:handler` 元素用于装配 `TerminateHandler`。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:terminator="http://dwarfeng.com/schema/spring-terminator"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/spring-terminator
        http://dwarfeng.com/schema/spring-terminator/spring-terminator.xsd"
>

    <terminator:config config-name="terminateConfig" pre-delay="1000" post-delay="500"/>
    <terminator:handler
            handler-name="${terminator.handler_name}"
            config-ref="${terminator.config_ref}"
    />
</beans>
```

#### 处理器名称

- **属性名**：`handler-name`。
- **类型**：`String`。
- **默认值**：`terminateHandler`。
- **说明**：`TerminateHandler` 的 bean 名称。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:terminator="http://dwarfeng.com/schema/spring-terminator"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/spring-terminator
        http://dwarfeng.com/schema/spring-terminator/spring-terminator.xsd"
>

    <terminator:config/>
    <terminator:handler handler-name="myTerminateHandler"/>
</beans>
```

#### 配置引用

- **属性名**：`config-ref`。
- **类型**：`String`。
- **默认值**：`terminateConfig`。
- **说明**：引用 `TerminateConfig` bean。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:terminator="http://dwarfeng.com/schema/spring-terminator"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/spring-terminator
        http://dwarfeng.com/schema/spring-terminator/spring-terminator.xsd"
>

    <terminator:config config-name="myTerminateConfig" pre-delay="1000" post-delay="500"/>
    <terminator:handler config-ref="myTerminateConfig"/>
</beans>
```

### qos 配置

`terminator:qos` 元素用于装配 `TerminateQosService`。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:terminator="http://dwarfeng.com/schema/spring-terminator"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/spring-terminator
        http://dwarfeng.com/schema/spring-terminator/spring-terminator.xsd"
>

    <terminator:config/>
    <terminator:handler/>
    <terminator:qos
            service-name="${terminator.qos_service_name}"
            handler-ref="${terminator.handler_ref}"
            sem-ref="${terminator.sem_ref}"
    />
</beans>
```

#### 服务名称

- **属性名**：`service-name`。
- **类型**：`String`。
- **默认值**：`terminatorQosService`。
- **说明**：`TerminateQosService` 的 bean 名称。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:terminator="http://dwarfeng.com/schema/spring-terminator"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/spring-terminator
        http://dwarfeng.com/schema/spring-terminator/spring-terminator.xsd"
>

    <terminator:config/>
    <terminator:handler/>
    <terminator:qos service-name="myTerminatorQosService"/>
</beans>
```

#### 处理器引用

- **属性名**：`handler-ref`。
- **类型**：`String`。
- **默认值**：`terminateHandler`。
- **说明**：引用 `TerminateHandler` bean。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:terminator="http://dwarfeng.com/schema/spring-terminator"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/spring-terminator
        http://dwarfeng.com/schema/spring-terminator/spring-terminator.xsd"
>

    <terminator:config/>
    <terminator:handler handler-name="myTerminateHandler"/>
    <terminator:qos handler-ref="myTerminateHandler"/>
</beans>
```

#### 异常映射器引用

- **属性名**：`sem-ref`。
- **类型**：`String`。
- **默认值**：`mapServiceExceptionMapper`。
- **说明**：引用 `ServiceExceptionMapper` bean，用于将处理器异常映射为服务异常。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:context="http://www.springframework.org/schema/context"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:terminator="http://dwarfeng.com/schema/spring-terminator"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://dwarfeng.com/schema/spring-terminator
        http://dwarfeng.com/schema/spring-terminator/spring-terminator.xsd"
>

    <context:component-scan base-package="com.example.terminator.configuration"/>

    <terminator:config/>
    <terminator:handler/>
    <terminator:qos sem-ref="mapServiceExceptionMapper"/>
</beans>
```

## 终止能力调用

### TerminateHandler 接口说明

所有直接终止调用都通过 `com.dwarfeng.springterminator.stack.handler.TerminateHandler` 进行：

```java
public interface TerminateHandler {

    void exit() throws HandlerException;

    void exitAndRestart() throws HandlerException;

    void exit(int exitCode) throws HandlerException;

    void exitAndRestart(int exitCode) throws HandlerException;

    int getExitCode() throws HandlerException;

    boolean getRestartFlag() throws HandlerException;
}
```

核心语义如下：

- `exit`/`exitAndRestart` 用于触发终止流程。
- `getExitCode`/`getRestartFlag` 在终止动作触发前会阻塞等待。
- 重复触发终止请求时，后续请求会被忽略，保持首次请求的终止状态。

### TerminateQosService 接口说明

如果业务已使用 subgrade 服务层，建议使用 `com.dwarfeng.springterminator.stack.service.TerminateQosService`：

```java
public interface TerminateQosService extends Service {

    void exit() throws ServiceException;

    void exit(int exitCode) throws ServiceException;

    void exitAndRestart() throws ServiceException;

    void exitAndRestart(int exitCode) throws ServiceException;
}
```

该服务代理 `TerminateHandler` 的退出能力，并统一抛出 `ServiceException`。

## spring-telqos 集成

### 接入 shutdown 指令

在 `spring-telqos` 命名空间中扫描集成包：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:telqos="http://dwarfeng.com/schema/spring-telqos"
        xmlns="http://www.springframework.org/schema/beans"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dwarfeng.com/schema/spring-telqos
        http://dwarfeng.com/schema/spring-telqos/spring-telqos.xsd"
>

    <telqos:config>
        <telqos:connection-setting
                port="${telqos.port}"
                charset="${telqos.charset}"
                banner-url="classpath:telqos/my-banner.txt"
                whitelist-regex="${telqos.whitelist_regex}"
                blacklist-regex="${telqos.blacklist_regex}"
        />
        <telqos:command>
            <telqos:command-impl package-scan="com.dwarfeng.springterminator.api.integration.springtelqos"/>
        </telqos:command>
    </telqos:config>
    <telqos:handler/>
    <telqos:qos/>
</beans>
```

接入后可在 telnet 客户端执行 `shutdown` 指令，通过二次确认触发退出或重启。

## 最佳实践

### 延时参数建议

- `pre-delay` 用于给调用方返回确认信息或做短暂收尾。
- `post-delay` 用于在退出动作后保留最小观测窗口。
- 建议避免过大延时，防止业务线程长时间阻塞。

### 终止入口治理

- 建议在业务层封装统一终止入口，避免散落调用。
- 对管理类操作保留明确日志（调用来源、退出代码、是否重启）。

### 重启能力边界

- `exitAndRestart` 只负责设置重启标记并退出当前 Spring 上下文。
- 外部容器、脚本或进程管理器是否执行真正重启，取决于部署环境。

## 常见问题

### 找不到 mapServiceExceptionMapper

**问题**：使用 `<terminator:qos/>` 启动时报错，提示找不到 `mapServiceExceptionMapper`。

**解决方案**：

1. 手动声明一个名为 `mapServiceExceptionMapper` 的 `ServiceExceptionMapper` bean。
2. 或将 `sem-ref` 指向您已有的 `ServiceExceptionMapper` bean 名称。

### 延时参数非法

**问题**：启动时报 `preDelay/postDelay 非法`。

**解决方案**：

1. 检查 `pre-delay`、`post-delay` 是否小于 `-1`。
2. 建议使用 `-1`（不启用）或 `0` 以上的毫秒值。

### getExitCode/getRestartFlag 长时间阻塞

**问题**：调用 `getExitCode()` 或 `getRestartFlag()` 后线程一直等待。

**解决方案**：

1. 确认应用中确实触发了 `exit/exitAndRestart`。
2. 确认终止动作发生在同一个 `ApplicationContext` 对应的 `TerminateHandler` 上。

### shutdown 指令未生效

**问题**：telqos 中无法识别 `shutdown`。

**解决方案**：

1. 检查是否引入了 `spring-terminator-api`。
2. 检查 `telqos:command-impl` 的 `package-scan` 是否为 `com.dwarfeng.springterminator.api.integration.springtelqos`。
3. 检查 Spring 容器中是否已装配 `TerminateQosService`（`<terminator:qos/>`）。

## 附录

1. [字符画生成工具](https://www.bootschool.net/ascii) 默认的 banner 使用 `broadway` 字体生成。
