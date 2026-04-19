package com.dwarfeng.springterminator.node.config;

import com.dwarfeng.springterminator.impl.handler.TerminateHandlerImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import javax.annotation.Nonnull;

/**
 * TerminateHandler 的 BeanDefinitionParser。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class SpringTerminatorDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private static final String STANDARD_BEAN_NAME = "terminateHandler";

    @Override
    protected String getBeanClassName(@NonNull Element element) {
        return TerminateHandlerImpl.class.getCanonicalName();
    }

    @Nonnull
    @Override
    protected String resolveId(
            @NonNull Element element, @NonNull AbstractBeanDefinition definition, @NonNull ParserContext parserContext
    ) {
        if (element.hasAttribute("id") && StringUtils.hasText(element.getAttribute("id"))) {
            return element.getAttribute("id");
        }
        return STANDARD_BEAN_NAME;
    }

    @Override
    protected void doParse(
            @NonNull Element element, @NonNull ParserContext parserContext, @NonNull BeanDefinitionBuilder builder
    ) {
        try {
            if (element.hasAttribute("pre-delay")) {
                builder.addPropertyValue("preDelay", element.getAttribute("pre-delay"));
            }
            if (element.hasAttribute("post-delay")) {
                builder.addPropertyValue("postDelay", element.getAttribute("post-delay"));
            }
        } catch (Exception e) {
            parserContext.getReaderContext().error("转换数字时出现异常", e);
        }

        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        builder.setLazyInit(false);
    }
}
