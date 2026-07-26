package com.dwarfeng.springterminator.impl.handler;

import com.dwarfeng.springterminator.sdk.util.TerminateExceptionHelper;
import com.dwarfeng.springterminator.stack.exception.TerminateException;
import com.dwarfeng.springterminator.stack.handler.TerminateHandler;
import com.dwarfeng.springterminator.stack.struct.TerminateConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TerminateHandler 的实现。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class TerminateHandlerImpl implements TerminateHandler, ApplicationListener<ApplicationEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminateHandlerImpl.class);

    private final ApplicationContext ctx;
    private final TerminateConfig terminateConfig;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean launchingFlag = true;
    private boolean runningFlag = true;
    private int exitCode = 0;
    private boolean restartFlag = false;
    private boolean postBlockFlag = false;

    public TerminateHandlerImpl(ApplicationContext ctx, TerminateConfig terminateConfig) {
        this.ctx = ctx;
        this.terminateConfig = terminateConfig;
    }

    @Override
    public void exit() throws TerminateException {
        exit(0);
    }

    @Override
    public void exitAndRestart() throws TerminateException {
        exitAndRestart(0);
    }

    @Override
    public void exit(int exitCode) throws TerminateException {
        LOGGER.info("程序退出, exitCode = {}", exitCode);

        lock.lock();
        try {
            internalExit(exitCode, false);
        } catch (Exception e) {
            throw TerminateExceptionHelper.parse(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void exitAndRestart(int exitCode) throws TerminateException {
        LOGGER.info("程序退出并重启, exitCode = {}", exitCode);

        lock.lock();
        try {
            internalExit(exitCode, true);
        } catch (Exception e) {
            throw TerminateExceptionHelper.parse(e);
        } finally {
            lock.unlock();
        }
    }

    private void internalExit(int exitCode, boolean restartFlag) {
        checkApplicationContextAvailable();

        if (postBlockFlag || !runningFlag) {
            LOGGER.info("终止流程已触发，忽略重复请求, exitCode = {}, restartFlag = {}", this.exitCode, this.restartFlag);
            return;
        }

        // 当程序设置延迟时，进行延时。
        long preDelay = terminateConfig.getPreDelay();
        if (preDelay > 0) {
            try {
                LOGGER.info("TerminateHandler 设置了前置延时, 等待 {} 毫秒...", preDelay);
                Thread.sleep(preDelay);
            } catch (InterruptedException ignored) {
            }
        }

        this.postBlockFlag = true;
        this.exitCode = exitCode;
        this.restartFlag = restartFlag;
        ((AbstractApplicationContext) ctx).stop();
        ((AbstractApplicationContext) ctx).close();

        // 当程序设置延迟时，进行延时。
        long postDelay = terminateConfig.getPostDelay();
        if (postDelay > 0) {
            long timeMeasure = -System.currentTimeMillis();
            try {
                LOGGER.info("TerminateHandler 设置了后置延时, 等待 {} 毫秒...", postDelay);
                Thread.sleep(postDelay);
            } catch (InterruptedException e) {
                timeMeasure += System.currentTimeMillis();
                LOGGER.info(
                        "后置延时被中断，当前线程名称为 {}，实际延时时间 {} 毫秒",
                        Thread.currentThread().getName(),
                        timeMeasure
                );
            }
        }

        // 取消 postBlockFlag 的置位，并对 condition 进行 signalAll 操作。
        this.postBlockFlag = false;
        condition.signalAll();
    }

    @Override
    public int getExitCode() throws TerminateException {
        lock.lock();
        try {
            // 确认程序是否停止。
            while (launchingFlag || runningFlag || postBlockFlag) {
                condition.awaitUninterruptibly();
            }

            // 返回最终的退出代码。
            return this.exitCode;
        } catch (Exception e) {
            throw TerminateExceptionHelper.parse(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean getRestartFlag() throws TerminateException {
        lock.lock();
        try {
            // 确认程序是否停止。
            while (runningFlag || postBlockFlag) {
                condition.awaitUninterruptibly();
            }

            // 返回最终重启标记。
            return this.restartFlag;
        } catch (Exception e) {
            throw TerminateExceptionHelper.parse(e);
        } finally {
            lock.unlock();
        }
    }

    private void checkApplicationContextAvailable() {
        if (Objects.isNull(ctx)) {
            throw new IllegalStateException("ctx 为 null");
        }
        if (!(ctx instanceof AbstractApplicationContext)) {
            throw new IllegalStateException("ctx 不是 AbstractApplicationContext 的实例");
        }
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationEvent event) {
        if (event instanceof ContextStartedEvent) {
            handleStarted();
        } else if (event instanceof ContextClosedEvent) {
            handleClosed();
        }
    }

    private void handleStarted() {
        lock.lock();
        try {
            TerminateHandlerImpl.this.launchingFlag = false;
            TerminateHandlerImpl.this.condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private void handleClosed() {
        lock.lock();
        try {
            TerminateHandlerImpl.this.runningFlag = false;
            TerminateHandlerImpl.this.condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
