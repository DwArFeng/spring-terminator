package com.dwarfeng.springterminator.api.configuration;

import com.dwarfeng.springterminator.sdk.exception.ServiceExceptionCodeSuppliers;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionCodeOffsetConfiguration {

    @Value("${terminator.exception_code_offset}")
    private int exceptionCodeOffset;
    @Value("${terminator.exception_code_offset.subgrade}")
    private int subgradeExceptionCodeOffset;
    @Value("${terminator.exception_code_offset.telqos}")
    private int telqosExceptionCodeOffset;

    @PostConstruct
    public void init() {
        ServiceExceptionCodeSuppliers.setExceptionCodeOffset(exceptionCodeOffset);
        com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionCodeSuppliers.setExceptionCodeOffset(
                subgradeExceptionCodeOffset
        );
        com.dwarfeng.springtelqos.sdk.exception.ServiceExceptionCodeSuppliers.setExceptionCodeOffset(
                telqosExceptionCodeOffset
        );
    }
}
