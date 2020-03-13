package com.dwarfeng.springterminator.node.config;

import com.dwarfeng.springterminator.impl.handler.TerminatorImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author DwArFeng
 * @since 1.0.0
 */
public class TerminalBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected String getBeanClassName(Element element) {
        return TerminatorImpl.class.getCanonicalName();
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        long preDelay = -1L;
        long postDelay = -1L;
        try {
            if (element.hasAttribute("pre-delay")) {
                preDelay = Long.parseLong(element.getAttribute("pre-delay"));
            }
            if (element.hasAttribute("post-delay")) {
                postDelay = Long.parseLong(element.getAttribute("post-delay"));
            }
        } catch (Exception e) {
            parserContext.getReaderContext().error("转换数字时出现异常", e);
        }

        builder.addPropertyValue("preDelay", preDelay);
        builder.addPropertyValue("postDelay", postDelay);

        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        builder.setLazyInit(false);
    }
}
