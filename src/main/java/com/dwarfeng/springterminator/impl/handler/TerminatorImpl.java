package com.dwarfeng.springterminator.impl.handler;

import com.dwarfeng.springterminator.stack.handler.Terminator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.lang.NonNull;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Terminator 的实现。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class TerminatorImpl implements Terminator, ApplicationContextAware, ApplicationListener<ContextClosedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminatorImpl.class);

    private AbstractApplicationContext applicationContext;
    private long preDelay = -1L;
    private long postDelay = -1L;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean runningFlag = true;
    private int exitCode = 0;
    private boolean restartFlag = false;
    private boolean postBlockFlag = false;

    @Override
    public void exit() {
        exit(0);
    }

    @Override
    public void exitAndRestart() {
        exitAndRestart(0);
    }

    @Override
    public void exit(int exitCode) {
        LOGGER.info("程序退出, exitCode = " + exitCode);

        lock.lock();
        try {
            internalExit(exitCode, false);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void exitAndRestart(int exitCode) {
        LOGGER.info("程序退出并重启, exitCode = " + exitCode);

        lock.lock();
        try {
            internalExit(exitCode, true);
        } finally {
            lock.unlock();
        }
    }

    private void internalExit(int exitCode, boolean restartFlag) {
        // 当程序设置延迟时，进行延时。
        if (this.preDelay > 0) {
            try {
                LOGGER.info("Terminator设置了前置延时, 等待 " + preDelay + " 毫秒...");
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
            try {
                LOGGER.info("Terminator设置了后置延时, 等待 " + postDelay + " 毫秒...");
                Thread.sleep(this.postDelay);
            } catch (InterruptedException ignored) {
            }
        }

        // 取消 postBlockFlag 的置位，并对 condition 进行 signalAll 操作。
        this.postBlockFlag = false;
        condition.signalAll();
    }

    @Override
    public int getExitCode() {
        lock.lock();
        try {
            // 确认程序是否停止。
            while (runningFlag || postBlockFlag) {
                condition.awaitUninterruptibly();
            }

            // 返回最终的退出代码。
            return this.exitCode;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean getRestartFlag() {
        lock.lock();
        try {
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
            throw new IllegalArgumentException("程序目前仅支持 AbstractApplicationContext 的子类");
        }
        this.applicationContext = (AbstractApplicationContext) applicationContext;
    }

    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent event) {
        lock.lock();
        try {
            TerminatorImpl.this.runningFlag = false;
            TerminatorImpl.this.condition.signalAll();
        } finally {
            lock.unlock();
        }
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
