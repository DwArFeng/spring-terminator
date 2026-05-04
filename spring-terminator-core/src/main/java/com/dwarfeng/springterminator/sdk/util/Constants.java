package com.dwarfeng.springterminator.sdk.util;

/**
 * 常量类。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class Constants {

    /**
     * @since 2.0.0
     */
    public static final String XSD_DEFAULT_TERMINATE_CONFIG_NAME = "terminateConfig";

    /**
     * @since 2.0.0
     */
    public static final String XSD_DEFAULT_TERMINATE_HANDLER_NAME = "terminateHandler";

    /**
     * @since 2.0.0
     */
    public static final String XSD_DEFAULT_TERMINATOR_QOS_SERVICE_NAME = "terminatorQosService";

    /**
     * @since 2.0.0
     */
    public static final String XSD_DEFAULT_SERVICE_EXCEPTION_MAPPER_NAME = "mapServiceExceptionMapper";

    private Constants() {
        throw new IllegalStateException("禁止实例化");
    }
}
