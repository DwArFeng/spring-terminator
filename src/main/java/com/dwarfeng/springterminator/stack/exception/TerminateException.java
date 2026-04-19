package com.dwarfeng.springterminator.stack.exception;

import com.dwarfeng.subgrade.stack.exception.HandlerException;

/**
 * 终止处理器异常。
 *
 * <p>
 * 该异常是 spring-terminator 中所有处理器异常的父类。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class TerminateException extends HandlerException {

    private static final long serialVersionUID = -3150743759511570757L;

    public TerminateException() {
    }

    public TerminateException(String message) {
        super(message);
    }

    public TerminateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TerminateException(Throwable cause) {
        super(cause);
    }
}
