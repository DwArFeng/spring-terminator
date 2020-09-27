package com.dwarfeng.springterminator.sdk.util;

import com.dwarfeng.springterminator.stack.handler.Terminator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Objects;
import java.util.function.Consumer;

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
        internalLaunch(new String[]{configLocation}, null);
    }

    /**
     * 启动程序。
     *
     * @param configLocation 配置文件的地址。
     * @param consumer       程序启动后用于后续初始化的消费者。
     * @since 1.0.5
     */
    public static void launch(String configLocation, Consumer<ApplicationContext> consumer) {
        internalLaunch(new String[]{configLocation}, consumer);
    }

    /**
     * 启动程序。
     *
     * @param configLocations 配置文件的地址组成的数组。
     * @since 1.0.3
     */
    public static void launch(String... configLocations) {
        internalLaunch(configLocations, null);
    }

    /**
     * 启动程序。
     *
     * @param configLocations 配置文件的地址组成的数组。
     * @param consumer        程序启动后用于后续初始化的消费者。
     * @since 1.0.5
     */
    public static void launch(String[] configLocations, Consumer<ApplicationContext> consumer) {
        internalLaunch(configLocations, consumer);
    }

    private static void internalLaunch(String[] configLocations, Consumer<ApplicationContext> consumer) {
        int exitCode;
        boolean restart;
        do {
            ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(configLocations);
            ctx.registerShutdownHook();
            ctx.start();
            if (Objects.nonNull(consumer)) {
                consumer.accept(ctx);
            }
            Terminator terminator = ctx.getBean(Terminator.class);
            exitCode = terminator.getExitCode();
            restart = terminator.getRestartFlag();
        } while (restart);
        System.exit(exitCode);
    }

    private ApplicationUtil() {
        throw new IllegalStateException("禁止外部实例化");
    }
}
