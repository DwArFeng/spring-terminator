package com.dwarfeng.springterminator.node.configuration;

import com.dwarfeng.subgrade.basic.impl.exception.MapServiceExceptionMapper;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 测试与示例用的 {@link com.dwarfeng.subgrade.basic.stack.exception.ServiceExceptionMapper} 配置。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
@Configuration
public class ServiceExceptionMapperConfiguration {

    @Bean
    public MapServiceExceptionMapper mapServiceExceptionMapper() {
        Map<Class<? extends Exception>, Supplier<ServiceException.Code>> des =
                com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionHelper.putDefaultDestination(null);
        des = com.dwarfeng.springterminator.sdk.exception.ServiceExceptionHelper.putDefaultDestination(des);
        return new MapServiceExceptionMapper(
                des, com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionCodeSuppliers.UNDEFINED
        );
    }
}
