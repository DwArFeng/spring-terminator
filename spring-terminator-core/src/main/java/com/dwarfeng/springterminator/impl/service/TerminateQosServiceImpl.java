package com.dwarfeng.springterminator.impl.service;

import com.dwarfeng.springterminator.stack.handler.TerminateHandler;
import com.dwarfeng.springterminator.stack.service.TerminateQosService;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;

/**
 * {@link TerminateQosService} 的实现。
 *
 * <p>
 * 通过构造注入持有 {@link TerminateHandler} 与 {@link ServiceExceptionMapper}，
 * 将处理器调用中的异常统一映射为 {@link ServiceException}。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class TerminateQosServiceImpl implements TerminateQosService {

    private final TerminateHandler terminateHandler;
    private final ServiceExceptionMapper sem;

    public TerminateQosServiceImpl(TerminateHandler terminateHandler, ServiceExceptionMapper sem) {
        this.terminateHandler = terminateHandler;
        this.sem = sem;
    }

    @Override
    public void exit() throws ServiceException {
        try {
            terminateHandler.exit();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("退出程序时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void exit(int exitCode) throws ServiceException {
        try {
            terminateHandler.exit(exitCode);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("以指定代码退出程序时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void exitAndRestart() throws ServiceException {
        try {
            terminateHandler.exitAndRestart();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("退出程序并重启时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void exitAndRestart(int exitCode) throws ServiceException {
        try {
            terminateHandler.exitAndRestart(exitCode);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("以指定代码退出程序并重启时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
