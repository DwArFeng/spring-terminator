module com.dwarfeng.springterminator.api {

    requires transitive com.dwarfeng.springtelqos.core;
    requires transitive com.dwarfeng.springterminator.core;
    requires org.apache.commons.cli;
    requires org.apache.commons.lang3;
    requires org.slf4j;

    exports com.dwarfeng.springterminator.api.integration.springtelqos;
}
