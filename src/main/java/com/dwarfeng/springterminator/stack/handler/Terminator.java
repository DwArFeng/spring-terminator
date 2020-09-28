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
     *
     * <p>
     * 退出动作分为三步：
     * <ul>
     * <li>1. 执行前置延时</li>
     * <li>2. 执行退出动作</li>
     * <li>3. 执行后置延时</li>
     * </ul>
     * 其中，后置延时在退出动作之后，如果后置延时执行的线程被Spring托管，则可能在执行后置延时时产生中断。
     * 如果产生中断，则后置延时会提前结束。
     */
    void exit();

    /**
     * 退出程序并重启。
     *
     * <p>
     * 退出并重启的步骤与退出一致，在程序退出时会将重启标记置位，
     * 调用该方法后，继续调用 {@link #getRestartFlag()} 会返回 <code>true</code>
     *
     * <p> 注意：部分第三方框架不支持重启（如 dubbo），请在开发时慎用该功能。
     */
    void exitAndRestart();

    /**
     * 以指定的退出代码退出程序。
     *
     * <p>
     * 退出动作分为三步：
     * <ul>
     * <li>1. 执行前置延时</li>
     * <li>2. 执行退出动作</li>
     * <li>3. 执行后置延时</li>
     * </ul>
     * 其中，后置延时在退出动作之后，如果后置延时执行的线程被Spring托管，则可能在执行后置延时时产生中断。
     * 如果产生中断，则后置延时会提前结束。
     *
     * @param exitCode 指定的退出代码。
     */
    void exit(int exitCode);

    /**
     * 以指定的退出代码退出程序并重启。
     *
     * <p>
     * 退出并重启的步骤与退出一致，在程序退出时会将重启标记置位，
     * 调用该方法后，继续调用 {@link #getRestartFlag()} 会返回 <code>true</code>
     *
     * <p> 注意：部分第三方框架不支持重启（如 dubbo），请在开发时慎用该功能。
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
