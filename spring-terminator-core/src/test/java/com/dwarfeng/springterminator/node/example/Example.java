package com.dwarfeng.springterminator.node.example;

import com.dwarfeng.springterminator.stack.handler.TerminateHandler;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

/**
 * spring-terminator 交互式示例。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class Example {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:spring/application-context*.xml"
        );
        ctx.registerShutdownHook();
        ctx.start();

        TerminateHandler terminateHandler = ctx.getBean(TerminateHandler.class);

        Scanner scanner = new Scanner(System.in);

        // 显示欢迎信息并等待开发者确认。
        System.out.println("开发者您好!");
        System.out.println("这是一个示例, 用于演示 spring-terminator 的终止能力");
        System.out.println("您可以在本示例中选择不同的终止模式, 观察退出代码与重启标记");
        System.out.print("请按回车键开始示例...");
        scanner.nextLine();

        // 1. 展示可选的终止模式。
        System.out.println();
        System.out.println("1. 请选择终止模式...");
        System.out.println("1) 调用 exit(0)");
        System.out.println("2) 调用 exit(1)");
        System.out.println("3) 调用 exitAndRestart(0)");
        System.out.println("4) 调用 exitAndRestart(1)");
        System.out.println("请输入选项序号, 不填默认为 1...");
        String mode = scanner.nextLine();
        if (mode == null || mode.trim().isEmpty()) {
            mode = "1";
        }

        // 2. 根据选项触发终止动作。
        System.out.println();
        System.out.println("2. 触发终止动作...");
        try {
            switch (mode) {
                case "1":
                    terminateHandler.exit(0);
                    break;
                case "2":
                    terminateHandler.exit(1);
                    break;
                case "3":
                    terminateHandler.exitAndRestart(0);
                    break;
                case "4":
                    terminateHandler.exitAndRestart(1);
                    break;
                default:
                    System.out.println("未知选项, 默认执行 exit(0)...");
                    terminateHandler.exit(0);
                    break;
            }
        } catch (HandlerException e) {
            throw new IllegalStateException("触发终止动作时发生异常", e);
        }

        // 3. 读取终止结果并输出。
        int exitCode;
        boolean restartFlag;
        try {
            exitCode = terminateHandler.getExitCode();
            restartFlag = terminateHandler.getRestartFlag();
        } catch (HandlerException e) {
            throw new IllegalStateException("读取终止状态时发生异常", e);
        }
        System.out.println("程序终止流程已执行完毕");
        System.out.println("exitCode = " + exitCode);
        System.out.println("restartFlag = " + restartFlag);
        System.out.println("示例演示完毕, 感谢您测试与使用!");

        System.exit(exitCode);
    }
}
