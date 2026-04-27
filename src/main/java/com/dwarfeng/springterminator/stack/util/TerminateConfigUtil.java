package com.dwarfeng.springterminator.stack.util;

import com.dwarfeng.springterminator.stack.struct.TerminateConfig;

/**
 * 终止处理器配置工具类。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class TerminateConfigUtil {

    /**
     * 检查前置延时是否合法。
     *
     * <p>
     * 合法范围为 <code>[-1, Long.MAX_VALUE]</code>，其中 -1 表示不启用延时。
     *
     * @param preDelay 前置延时。
     * @throws IllegalArgumentException 若参数非法。
     */
    public static void checkPreDelay(long preDelay) {
        if (preDelay < TerminateConfig.Builder.MIN_DELAY) {
            throw new IllegalArgumentException(
                    "preDelay 非法, 不能小于 " + TerminateConfig.Builder.MIN_DELAY + ", 当前值: " + preDelay
            );
        }
    }

    /**
     * 检查后置延时是否合法。
     *
     * <p>
     * 合法范围为 <code>[-1, Long.MAX_VALUE]</code>，其中 -1 表示不启用延时。
     *
     * @param postDelay 后置延时。
     * @throws IllegalArgumentException 若参数非法。
     */
    public static void checkPostDelay(long postDelay) {
        if (postDelay < TerminateConfig.Builder.MIN_DELAY) {
            throw new IllegalArgumentException(
                    "postDelay 非法, 不能小于 " + TerminateConfig.Builder.MIN_DELAY + ", 当前值: " + postDelay
            );
        }
    }

    private TerminateConfigUtil() {
        throw new IllegalStateException("禁止实例化");
    }
}
