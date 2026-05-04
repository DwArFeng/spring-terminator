package com.dwarfeng.springterminator.node.config;

import com.dwarfeng.springterminator.impl.handler.TerminateHandlerImpl;
import com.dwarfeng.springterminator.sdk.util.BeanDefinitionParserUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import javax.annotation.Nonnull;

/**
 * TerminateHandler 的 BeanDefinitionParser。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class SpringTerminatorDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, @Nonnull ParserContext parserContext) {
        String handlerName = (String) BeanDefinitionParserUtil.mayResolveSpel(
                parserContext, element.getAttribute("handler-name")
        );
        String configRef = (String) BeanDefinitionParserUtil.mayResolveSpel(
                parserContext, element.getAttribute("config-ref")
        );

        BeanDefinitionParserUtil.makeSureBeanNameNotDuplicated(parserContext, handlerName);

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(TerminateHandlerImpl.class);
        builder.getRawBeanDefinition().setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addIndexedArgumentValue(1, new RuntimeBeanReference(configRef));
        builder.getRawBeanDefinition().setConstructorArgumentValues(constructorArgumentValues);
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        builder.setLazyInit(false);
        parserContext.getRegistry().registerBeanDefinition(handlerName, builder.getBeanDefinition());
        return null;
    }
}
