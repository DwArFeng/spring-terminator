package com.dwarfeng.springterminator.sdk.exception;

import com.dwarfeng.springterminator.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.springterminator.core.internal.i18n.CoreMessages;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;

import java.util.function.Supplier;

/**
 * Terminator 模块服务异常代码供应器。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class ServiceExceptionCodeSuppliers {

    private static volatile int EXCEPTION_CODE_OFFSET = 22000;

    /**
     * 终止操作失败。
     */
    public static final Supplier<ServiceException.Code> TERMINATE_FAILED =
            () -> new ServiceException.Code(
                    offset(0), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_TERMINATE_FAILED)
            );

    // 为了程序的可扩展性，此处不做代码简化。
    @SuppressWarnings("SameParameterValue")
    private static int offset(int i) {
        return EXCEPTION_CODE_OFFSET + i;
    }

    /**
     * 获取异常代码的偏移量。
     *
     * @return 异常代码的偏移量。
     */
    public static int getExceptionCodeOffset() {
        return EXCEPTION_CODE_OFFSET;
    }

    /**
     * 设置异常代码的偏移量。
     *
     * <p>
     * 该方法只更新后续生成异常代码所使用的偏移量，已经创建的异常代码保持不变。
     *
     * @param exceptionCodeOffset 指定的异常代码偏移量。
     */
    public static void setExceptionCodeOffset(int exceptionCodeOffset) {
        EXCEPTION_CODE_OFFSET = exceptionCodeOffset;
    }

    private ServiceExceptionCodeSuppliers() {
        throw new IllegalStateException("禁止实例化");
    }
}
