package com.dwarfeng.springterminator.node.config;

import com.dwarfeng.springterminator.impl.service.TerminateQosServiceImpl;
import com.dwarfeng.springterminator.sdk.util.BeanDefinitionParserUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import javax.annotation.Nonnull;

/**
 * Terminator {@code qos} 元素的 BeanDefinitionParser。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class SpringTerminatorQosDefinitionParser implements BeanDefinitionParser {

    private static final String STANDARD_HANDLER_NAME = "terminateHandler";
    private static final String STANDARD_SERVICE_NAME = "terminatorQosService";
    private static final String STANDARD_SEM_REF = "mapServiceExceptionMapper";

    @Override
    public BeanDefinition parse(Element element, @Nonnull ParserContext parserContext) {
        String handlerName = BeanDefinitionParserUtil.mayResolvePlaceholder(
                parserContext, element.getAttribute("handler-name")
        );
        String serviceName = BeanDefinitionParserUtil.mayResolvePlaceholder(
                parserContext, element.getAttribute("service-name")
        );
        String semRef = BeanDefinitionParserUtil.mayResolvePlaceholder(
                parserContext, element.getAttribute("sem-ref")
        );

        if (!StringUtils.hasText(handlerName)) {
            handlerName = STANDARD_HANDLER_NAME;
        }
        if (!StringUtils.hasText(serviceName)) {
            serviceName = STANDARD_SERVICE_NAME;
        }
        if (!StringUtils.hasText(semRef)) {
            semRef = STANDARD_SEM_REF;
        }

        BeanDefinitionParserUtil.makeSureBeanNameNotDuplicated(parserContext, serviceName);

        BeanDefinitionBuilder qosServiceBuilder = BeanDefinitionBuilder.rootBeanDefinition(
                TerminateQosServiceImpl.class
        );
        qosServiceBuilder.getRawBeanDefinition().setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addIndexedArgumentValue(0, new RuntimeBeanReference(handlerName));
        constructorArgumentValues.addIndexedArgumentValue(1, new RuntimeBeanReference(semRef));
        qosServiceBuilder.getRawBeanDefinition().setConstructorArgumentValues(constructorArgumentValues);
        qosServiceBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
        qosServiceBuilder.setLazyInit(false);
        parserContext.getRegistry().registerBeanDefinition(serviceName, qosServiceBuilder.getBeanDefinition());

        return null;
    }
}
