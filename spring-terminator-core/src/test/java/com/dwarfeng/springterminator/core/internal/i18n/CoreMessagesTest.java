package com.dwarfeng.springterminator.core.internal.i18n;

import com.dwarfeng.springterminator.base.sdk.i18n.MessageContext;
import com.dwarfeng.springterminator.base.sdk.i18n.Messages;
import com.dwarfeng.springterminator.base.stack.i18n.MessageCatalog;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Core 模块消息测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class CoreMessagesTest {

    @Test
    public void resolvesSimplifiedChineseMessage() {
        assertEquals(
                "终止操作失败",
                CoreMessages.message(Locale.SIMPLIFIED_CHINESE, CoreMessageKey.SERVICE_EXCEPTION_TERMINATE_FAILED)
        );
    }

    @Test
    public void resolvesEnglishFallbackMessage() {
        assertEquals(
                "Terminate operation failed",
                MessageContext.call(
                        Locale.US, () -> CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_TERMINATE_FAILED)
                )
        );
    }

    @Test
    public void degradesMissingMessageKeyForEveryCatalog() {
        for (CoreMessages.Catalog catalog : CoreMessages.Catalog.values()) {
            assertEquals(
                    "!unknown.message.key!",
                    Messages.resolve(catalog.messageCatalog(), "unknown.message.key", Locale.US)
            );
        }
    }

    @Test
    public void keepsMessageKeyResourcesAlignedByCatalog() throws IOException {
        for (CoreMessages.Catalog catalog : CoreMessages.Catalog.values()) {
            List<CoreMessageKey> catalogKeys = Arrays.stream(CoreMessageKey.values())
                    .filter(key -> key.catalog() == catalog)
                    .toList();
            Set<String> enumKeys = catalogKeys.stream()
                    .map(CoreMessageKey::key)
                    .collect(Collectors.toUnmodifiableSet());
            assertEquals(catalogKeys.size(), enumKeys.size(), () -> "Duplicate message key in catalog: " + catalog);
            assertEquals(enumKeys, loadKeys(catalog, ".properties"));
            assertEquals(enumKeys, loadKeys(catalog, "_zh_CN.properties"));
        }
    }

    @Test
    public void cachesOneMessageCatalogPerCatalog() {
        for (CoreMessages.Catalog catalog : CoreMessages.Catalog.values()) {
            MessageCatalog messageCatalog = catalog.messageCatalog();
            Arrays.stream(CoreMessageKey.values())
                    .filter(key -> key.catalog() == catalog)
                    .forEach(key -> assertSame(messageCatalog, key.catalog().messageCatalog()));
        }
    }

    private Set<String> loadKeys(CoreMessages.Catalog catalog, String resourceSuffix) throws IOException {
        String resourceName = catalog.messageCatalog().baseName().replace('.', '/') + resourceSuffix;
        try (InputStream inputStream = CoreMessages.class.getModule().getResourceAsStream(resourceName)) {
            assertNotNull(inputStream, () -> "Missing message resource: " + resourceName);
            Properties properties = new Properties();
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            return properties.stringPropertyNames();
        }
    }
}
