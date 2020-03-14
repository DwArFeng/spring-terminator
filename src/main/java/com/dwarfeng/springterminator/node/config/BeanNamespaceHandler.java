package com.dwarfeng.springterminator.node.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 终结者命名空间处理器。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class BeanNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("bean", new BeanDefinitionParser());
    }
}
