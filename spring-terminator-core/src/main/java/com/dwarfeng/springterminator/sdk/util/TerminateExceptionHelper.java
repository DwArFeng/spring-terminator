package com.dwarfeng.springterminator.sdk.util;

import com.dwarfeng.springterminator.stack.exception.TerminateException;

import org.jetbrains.annotations.NotNull;

/**
 * 终止处理器异常帮助类。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class TerminateExceptionHelper {

    /**
     * 将指定的异常转化为终止处理器异常。
     *
     * @param e 指定的异常。
     * @return 解析后得到的终止处理器异常。
     */
    public static TerminateException parse(@NotNull Exception e) {
        if (e instanceof TerminateException) {
            return (TerminateException) e;
        }
        return new TerminateException(e);
    }

    private TerminateExceptionHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
