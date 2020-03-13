package com.dwarfeng.springterminator.sdk.util;

import com.dwarfeng.springterminator.stack.handler.Terminator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 应用工具类。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class ApplicationUtil {

    /**
     * 启动程序。
     *
     * @param configLocation 配置文件的地址。
     */
    public static void launch(String configLocation) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(configLocation);
        ctx.registerShutdownHook();
        ctx.start();
        Terminator terminator = ctx.getBean(Terminator.class);
        System.exit(terminator.getExitCode());
    }

    private ApplicationUtil() {
        throw new IllegalStateException("禁止外部实例化");
    }
}
