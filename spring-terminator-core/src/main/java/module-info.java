module com.dwarfeng.springterminator.core {

    requires transitive com.dwarfeng.dutil.basic;
    requires com.dwarfeng.springterminator.base;
    requires transitive com.dwarfeng.subgrade.basic;
    requires org.apache.commons.lang3;
    requires org.slf4j;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.expression;
    requires static org.jetbrains.annotations;
    requires transitive java.xml;

    exports com.dwarfeng.springterminator.impl.handler;
    exports com.dwarfeng.springterminator.impl.service;
    exports com.dwarfeng.springterminator.node.configuration;
    exports com.dwarfeng.springterminator.sdk.exception;
    exports com.dwarfeng.springterminator.sdk.util;
    exports com.dwarfeng.springterminator.stack.exception;
    exports com.dwarfeng.springterminator.stack.handler;
    exports com.dwarfeng.springterminator.stack.service;
    exports com.dwarfeng.springterminator.stack.struct;
    exports com.dwarfeng.springterminator.stack.util;

    opens com.dwarfeng.springterminator.sdk.i18n to com.dwarfeng.springterminator.base;
}
