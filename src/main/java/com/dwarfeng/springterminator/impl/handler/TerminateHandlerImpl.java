package com.dwarfeng.springterminator.impl.handler;

import com.dwarfeng.springterminator.stack.exception.TerminateException;
import com.dwarfeng.springterminator.stack.handler.TerminateHandler;
import com.dwarfeng.subgrade.sdk.exception.HandlerExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.lang.NonNull;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TerminateHandler 的实现。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class TerminateHandlerImpl
        implements TerminateHandler, ApplicationContextAware, ApplicationListener<ApplicationEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminateHandlerImpl.class);

    private AbstractApplicationContext applicationContext;
    private TerminateException contextException;
    private long preDelay = -1L;
    private long postDelay = -1L;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean launchingFlag = true;
    private boolean runningFlag = true;
    private int exitCode = 0;
    private boolean restartFlag = false;
    private boolean postBlockFlag = false;

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
            throw parseTerminateException(e);
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
            throw parseTerminateException(e);
        } finally {
            lock.unlock();
        }
    }

    private void internalExit(int exitCode, boolean restartFlag) throws Exception {
        checkContextAvailable();

        if (postBlockFlag || !runningFlag) {
            LOGGER.info("终止流程已触发，忽略重复请求, exitCode = {}, restartFlag = {}", this.exitCode, this.restartFlag);
            return;
        }

        // 当程序设置延迟时，进行延时。
        if (this.preDelay > 0) {
            try {
                LOGGER.info("TerminateHandler 设置了前置延时, 等待 {} 毫秒...", preDelay);
                Thread.sleep(this.preDelay);
            } catch (InterruptedException ignored) {
            }
        }

        this.postBlockFlag = true;
        this.exitCode = exitCode;
        this.restartFlag = restartFlag;
        applicationContext.stop();
        applicationContext.close();

        // 当程序设置延迟时，进行延时。
        if (this.postDelay > 0) {
            long timeMeasure = -System.currentTimeMillis();
            try {
                LOGGER.info("TerminateHandler 设置了后置延时, 等待 {} 毫秒...", postDelay);
                Thread.sleep(this.postDelay);
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
            checkContextAvailable();

            // 确认程序是否停止。
            while (launchingFlag || runningFlag || postBlockFlag) {
                condition.awaitUninterruptibly();
            }

            // 返回最终的退出代码。
            return this.exitCode;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean getRestartFlag() throws TerminateException {
        lock.lock();
        try {
            checkContextAvailable();

            // 确认程序是否停止。
            while (runningFlag || postBlockFlag) {
                condition.awaitUninterruptibly();
            }

            // 返回最终重启标记。
            return this.restartFlag;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (!(applicationContext instanceof AbstractApplicationContext)) {
            contextException = new TerminateException(
                    "程序目前仅支持 AbstractApplicationContext 的子类, class = " + applicationContext.getClass()
            );
            return;
        }
        this.contextException = null;
        this.applicationContext = (AbstractApplicationContext) applicationContext;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent event) {
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

    private void checkContextAvailable() throws TerminateException {
        if (contextException != null) {
            throw contextException;
        }
        if (applicationContext == null) {
            throw new TerminateException("程序上下文尚未注入");
        }
    }

    private TerminateException parseTerminateException(Exception e) {
        HandlerException handlerException = HandlerExceptionHelper.parse(e);
        if (handlerException instanceof TerminateException) {
            return (TerminateException) handlerException;
        }
        return new TerminateException(handlerException);
    }

    public long getPreDelay() {
        return preDelay;
    }

    public void setPreDelay(long preDelay) {
        this.preDelay = preDelay;
    }

    public long getPostDelay() {
        return postDelay;
    }

    public void setPostDelay(long postDelay) {
        this.postDelay = postDelay;
    }
}
