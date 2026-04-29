package com.dwarfeng.springterminator.stack.service;

import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;

/**
 * 程序终止 QoS 服务。
 *
 * <p>
 * 该服务对 {@link com.dwarfeng.springterminator.stack.handler.TerminateHandler} 的退出类能力进行服务层封装，
 * 对外统一抛出 {@link ServiceException}，便于与 subgrade 服务层异常体系对齐。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public interface TerminateQosService extends Service {

    /**
     * 退出程序。
     *
     * <p>
     * 语义与 {@link com.dwarfeng.springterminator.stack.handler.TerminateHandler#exit()} 一致，
     * 由底层处理器完成前置延时、退出动作与后置延时。
     *
     * @throws ServiceException 服务异常。
     */
    void exit() throws ServiceException;

    /**
     * 以指定的退出代码退出程序。
     *
     * @param exitCode 指定的退出代码。
     * @throws ServiceException 服务异常。
     */
    void exit(int exitCode) throws ServiceException;

    /**
     * 退出程序并重启。
     *
     * <p>
     * 语义与 {@link com.dwarfeng.springterminator.stack.handler.TerminateHandler#exitAndRestart()} 一致。
     *
     * @throws ServiceException 服务异常。
     */
    void exitAndRestart() throws ServiceException;

    /**
     * 以指定的退出代码退出程序并重启。
     *
     * @param exitCode 指定的退出代码。
     * @throws ServiceException 服务异常。
     */
    void exitAndRestart(int exitCode) throws ServiceException;
}
