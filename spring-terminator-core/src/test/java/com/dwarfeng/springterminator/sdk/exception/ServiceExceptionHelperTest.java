package com.dwarfeng.springterminator.sdk.exception;

import com.dwarfeng.springterminator.stack.exception.TerminateException;
import com.dwarfeng.subgrade.basic.impl.exception.MapServiceExceptionMapper;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Terminator 模块服务异常帮助类测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class ServiceExceptionHelperTest {

    @Test
    public void shouldProvideDefaultDestinationForMapper() {
        Map<Class<? extends Exception>, Supplier<ServiceException.Code>> destination =
                ServiceExceptionHelper.putDefaultDestination(null);
        MapServiceExceptionMapper mapper = new MapServiceExceptionMapper(
                destination, com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionCodeSuppliers.UNDEFINED
        );

        ServiceException first = mapper.map(new TerminateException());
        ServiceException second = mapper.map(new TerminateException());

        assertSame(ServiceExceptionCodeSuppliers.TERMINATE_FAILED, destination.get(TerminateException.class));
        assertEquals(ServiceExceptionCodeSuppliers.getExceptionCodeOffset(), first.getCode().getCode());
        assertEquals(ServiceExceptionCodeSuppliers.getExceptionCodeOffset(), second.getCode().getCode());
        assertNotSame(first.getCode(), second.getCode());
    }

    @Test
    public void shouldResolveParentRouteSupplierForEveryMapping() {
        AtomicInteger calls = new AtomicInteger();
        Supplier<ServiceException.Code> supplier =
                () -> new ServiceException.Code(7000 + calls.incrementAndGet(), "parent");
        Map<Class<? extends Exception>, Supplier<ServiceException.Code>> destination = new HashMap<>();
        destination.put(Exception.class, supplier);
        MapServiceExceptionMapper mapper = new MapServiceExceptionMapper(
                destination, com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionCodeSuppliers.UNDEFINED
        );

        ServiceException first = mapper.map(new TerminateException());
        ServiceException second = mapper.map(new TerminateException());

        assertEquals(7001, first.getCode().getCode());
        assertEquals(7002, second.getCode().getCode());
        assertEquals(2, calls.get());
        assertNotSame(first.getCode(), second.getCode());
    }

    @Test
    public void shouldResolveDefaultSupplierForEveryMapping() {
        AtomicInteger calls = new AtomicInteger();
        Supplier<ServiceException.Code> supplier =
                () -> new ServiceException.Code(8000 + calls.incrementAndGet(), "default");
        MapServiceExceptionMapper mapper = new MapServiceExceptionMapper(new HashMap<>(), supplier);

        ServiceException first = mapper.map(new TerminateException());
        ServiceException second = mapper.map(new TerminateException());

        assertEquals(8001, first.getCode().getCode());
        assertEquals(8002, second.getCode().getCode());
        assertEquals(2, calls.get());
        assertNotSame(first.getCode(), second.getCode());
    }
}
