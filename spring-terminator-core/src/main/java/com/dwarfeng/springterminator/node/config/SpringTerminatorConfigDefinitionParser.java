package com.dwarfeng.springterminator.node.config;

import com.dwarfeng.springterminator.sdk.util.BeanDefinitionParserUtil;
import com.dwarfeng.springterminator.stack.struct.TerminateConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import javax.annotation.Nonnull;

/**
 * TerminateConfig 的 BeanDefinitionParser。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class SpringTerminatorConfigDefinitionParser implements BeanDefinitionParser {

    private static final String STANDARD_CONFIG_ID = "terminateConfig";

    @Override
    public BeanDefinition parse(Element element, @Nonnull ParserContext parserContext) {
        String configId = BeanDefinitionParserUtil.mayResolvePlaceholder(
                parserContext, element.getAttribute("config-id")
        );
        if (!StringUtils.hasText(configId)) {
            configId = STANDARD_CONFIG_ID;
        }
        BeanDefinitionParserUtil.makeSureBeanNameNotDuplicated(parserContext, configId);

        Object preDelay = resolveDelayAttribute(
                element, parserContext, "pre-delay", TerminateConfig.Builder.DEFAULT_PRE_DELAY
        );
        Object postDelay = resolveDelayAttribute(
                element, parserContext, "post-delay", TerminateConfig.Builder.DEFAULT_POST_DELAY
        );

        RootBeanDefinition configBuilderBeanDefinition = new RootBeanDefinition(TerminateConfig.Builder.class);
        configBuilderBeanDefinition.getPropertyValues().add("preDelay", preDelay);
        configBuilderBeanDefinition.getPropertyValues().add("postDelay", postDelay);
        configBuilderBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        configBuilderBeanDefinition.setLazyInit(false);
        String configBuilderBeanName = BeanDefinitionParserUtil.parseAvailableBeanName(
                parserContext, configId + "Builder"
        );
        parserContext.getRegistry().registerBeanDefinition(configBuilderBeanName, configBuilderBeanDefinition);

        RootBeanDefinition configBeanDefinition = new RootBeanDefinition(TerminateConfig.class);
        configBeanDefinition.setFactoryBeanName(configBuilderBeanName);
        configBeanDefinition.setFactoryMethodName("build");
        configBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        configBeanDefinition.setLazyInit(false);
        parserContext.getRegistry().registerBeanDefinition(configId, configBeanDefinition);

        return null;
    }

    private Object resolveDelayAttribute(
            Element element, ParserContext parserContext, String attributeName, long defaultValue
    ) {
        if (!element.hasAttribute(attributeName)) {
            return defaultValue;
        }
        String attributeValue = BeanDefinitionParserUtil.mayResolvePlaceholder(parserContext, element.getAttribute(attributeName));
        if (!StringUtils.hasText(attributeValue)) {
            return defaultValue;
        }
        return attributeValue;
    }
}
