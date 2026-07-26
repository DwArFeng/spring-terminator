package com.dwarfeng.springterminator.node.configuration;

import com.dwarfeng.springterminator.impl.service.TerminateQosServiceImpl;
import com.dwarfeng.springterminator.sdk.util.BeanDefinitionParserUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Terminator {@code qos} 元素的 BeanDefinitionParser。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class SpringTerminatorQosDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, @NotNull ParserContext parserContext) {
        String serviceName = (String) BeanDefinitionParserUtil.mayResolveSpel(
                parserContext, element.getAttribute("service-name")
        );
        String handlerRef = (String) BeanDefinitionParserUtil.mayResolveSpel(
                parserContext, element.getAttribute("handler-ref")
        );
        String semRef = (String) BeanDefinitionParserUtil.mayResolveSpel(
                parserContext, element.getAttribute("sem-ref")
        );

        BeanDefinitionParserUtil.makeSureBeanNameNotDuplicated(parserContext, serviceName);

        BeanDefinitionBuilder qosServiceBuilder = BeanDefinitionBuilder.rootBeanDefinition(
                TerminateQosServiceImpl.class
        );
        qosServiceBuilder.getRawBeanDefinition().setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addIndexedArgumentValue(0, new RuntimeBeanReference(handlerRef));
        constructorArgumentValues.addIndexedArgumentValue(1, new RuntimeBeanReference(semRef));
        qosServiceBuilder.getRawBeanDefinition().setConstructorArgumentValues(constructorArgumentValues);
        qosServiceBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
        qosServiceBuilder.setLazyInit(false);
        parserContext.getRegistry().registerBeanDefinition(serviceName, qosServiceBuilder.getBeanDefinition());

        return null;
    }
}
