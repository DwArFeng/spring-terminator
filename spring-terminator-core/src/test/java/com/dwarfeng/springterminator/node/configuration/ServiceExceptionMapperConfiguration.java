package com.dwarfeng.springterminator.node.configuration;

import com.dwarfeng.subgrade.impl.exception.MapServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 测试与示例用的 {@link com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper} 配置。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
@Configuration
public class ServiceExceptionMapperConfiguration {

    @Bean
    public MapServiceExceptionMapper mapServiceExceptionMapper() {
        Map<Class<? extends Exception>, ServiceException.Code> destination =
                com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper.putDefaultDestination(null);
        destination = com.dwarfeng.springterminator.sdk.util.ServiceExceptionHelper.putDefaultDestination(destination);
        return new MapServiceExceptionMapper(
                destination, com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes.UNDEFINED
        );
    }
}
