package com.dwarfeng.springterminator.api.example;

import com.dwarfeng.dutil.basic.sdk.io.CT;
import com.dwarfeng.springterminator.stack.handler.TerminateHandler;
import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Telqos 示例。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class Example {

    static void main() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:com/dwarfeng/springterminator/api/spring/application-context*.xml"
        );
        ctx.registerShutdownHook();
        ctx.start();

        TerminateHandler terminateHandler = ctx.getBean(TerminateHandler.class);

        // 显示欢迎信息。
        CT.trace("开发者您好!");
        CT.trace("这是一个示例, 用于演示 dwarfeng-spring-terminator 的功能");
        CT.trace("您可以使用 telnet 客户端工具访问本机 ${telqos.port} 端口以体验本示例的功能");
        CT.trace("您可以使用 telqos 的 shutdown 命令以关闭本示例...");

        // 等待 terminateHandler 返回退出代码和重启标记。
        int exitCode;
        boolean restartFlag;
        try {
            exitCode = terminateHandler.getExitCode();
            restartFlag = terminateHandler.getRestartFlag();
        } catch (HandlerException e) {
            throw new IllegalStateException("读取终止状态时发生异常", e);
        }

        CT.trace("退出代码: " + exitCode);
        CT.trace("重启标记: " + restartFlag);
        CT.trace("本示例不演示重启功能, 将会直接使用 0 作为退出代码退出程序");
        CT.trace("示例演示完毕, 感谢您测试与使用!");

        System.exit(exitCode);
    }
}
