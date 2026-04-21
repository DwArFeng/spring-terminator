# spring-terminator

一款基于 Spring 框架的优雅的程序终止器。

---

## 特性

1. Spring 自定义命名空间支持（terminator:bean）。
2. 前置延时支持（pre-delay）。
3. 后置延时支持（post-delay）。
4. 退出代码支持（exit code）。
5. 退出并重启支持（exitAndRestart）。
6. 优雅关闭 Spring 应用上下文。
7. 阻塞式获取退出代码和重启标识。

运行 `src/test` 下的测试类以观察全部特性。

| 测试类名                                                          | 说明     |
|---------------------------------------------------------------|--------|
| com.dwarfeng.springterminator.impl.handler.TerminatorImplTest | 基本功能测试 |

## 文档

该项目的文档位于 [docs](../../../docs) 目录下，包括：

### wiki

wiki 为项目的开发人员为本项目编写的详细文档，包含不同语言的版本，主要入口为：

1. [简介](./Introduction.md) - 即本文件。
2. [目录](./Contents.md) - 文档目录。

## 测试

该项目针对 Spring 框架进行了测试，测试结果如下：

| Spring 版本    | 测试结果 |
|--------------|------|
| Spring 5.3.x | 通过   |

## 安装说明

1. 下载源码。

   使用 git 进行源码下载。

   ```shell
   git clone git@github.com:DwArFeng/spring-terminator.git
   ```

   对于中国用户，可以使用 gitee 进行高速下载。

   ```shell
   git clone git@gitee.com:dwarfeng/spring-terminator.git
   ```

2. 项目安装。

   进入项目根目录，执行 maven 命令

   ```shell
   mvn clean source:jar install
   ```

3. 项目引入。

   在项目的 pom.xml 中添加如下依赖：

   ```xml
   <dependency>
       <groupId>com.dwarfeng</groupId>
       <artifactId>spring-terminator</artifactId>
       <version>${spring-terminator.version}</version>
   </dependency>
   ```

4. enjoy it.

## 如何使用

本项目仅支持单实例模式。本项目的目的是优雅停止 Spring 实例，一个 ApplicationContext 中，自然只需要一个 Terminator 实例。

1. 在 Spring 中添加如下配置。

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
   
       <terminator:bean pre-delay="1000" post-delay="500"/>
   </beans>
   ```

2. 使用 `ApplicationUtil.launch` 方法启动程序。

   ```java
   @SuppressWarnings("UnnecessaryModifier")
   public static void main(String[] args) {
       ApplicationUtil.launch("classpath:spring/application-context*.xml");
   }
   ```

   或者使用多个配置文件：

   ```java
   @SuppressWarnings("UnnecessaryModifier")
   public static void main(String[] args) {
       ApplicationUtil.launch(
           "classpath:spring/application-context-scan.xml",
           "classpath:spring/application-context-terminator.xml"
       );
   }
   ```

   或者使用启动后初始化消费者：

   ```java
   @SuppressWarnings("UnnecessaryModifier")
   public static void main(String[] args) {
       ApplicationUtil.launch(
           "classpath:spring/application-context*.xml",
           ctx -> {
               // 程序启动后的初始化逻辑
               System.out.println("ApplicationContext 已启动");
           }
       );
   }
   ```

3. 在 bean 中注入 `Terminator` 对象，随时随地，优雅的关闭程序。

   ```java
   @Component
   public class ProgramKiller {
   
       @Autowired
       private Terminator terminator;
   
       public void exit() {
           terminator.exit(0);
       }
   
       public void exitWithCode(int exitCode) {
           terminator.exit(exitCode);
       }
   
       public void exitAndRestart() {
           terminator.exitAndRestart(0);
       }
   }
   ```
