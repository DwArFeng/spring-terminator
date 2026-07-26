package com.dwarfeng.springterminator.core.internal.i18n;

import static com.dwarfeng.springterminator.core.internal.i18n.CoreMessages.Catalog.SDK;

/**
 * Core 模块消息键。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public enum CoreMessageKey {

    SERVICE_EXCEPTION_TERMINATE_FAILED(SDK, "service_exception.terminate_failed");

    private final CoreMessages.Catalog catalog;
    private final String key;

    CoreMessageKey(CoreMessages.Catalog catalog, String key) {
        this.catalog = catalog;
        this.key = key;
    }

    CoreMessages.Catalog catalog() {
        return catalog;
    }

    /**
     * 获取消息键。
     *
     * @return 消息键。
     */
    public String key() {
        return key;
    }
}
