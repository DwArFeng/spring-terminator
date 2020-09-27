package com.dwarfeng.springterminator.stack.handler;

/**
 * 程序终结者。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public interface Terminator {

    /**
     * 退出程序。
     */
    void exit();

    /**
     * 退出程序并重启。
     */
    void exitAndRestart();

    /**
     * 以指定的退出代码退出程序。
     *
     * @param exitCode 指定的退出代码。
     */
    void exit(int exitCode);

    /**
     * 以指定的退出代码退出程序并重启。
     *
     * @param exitCode 指定的退出代码。
     */
    void exitAndRestart(int exitCode);

    /**
     * 获取程序的退出代码。
     *
     * <p>在调用退出程序的方法之前，该方法将一直阻塞。
     *
     * @return 程序的退出代码。
     */
    int getExitCode();

    /**
     * 获取程序的重启标识。
     *
     * <p>在调用退出程序的方法之前，该方法将一直阻塞。
     *
     * @return 程序的重启标识。
     */
    boolean getRestartFlag();
}
