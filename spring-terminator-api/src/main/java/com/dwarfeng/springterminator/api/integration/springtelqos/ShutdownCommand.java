package com.dwarfeng.springterminator.api.integration.springtelqos;

import com.dwarfeng.springtelqos.sdk.command.CliCommand;
import com.dwarfeng.springtelqos.sdk.configuration.TelqosCommand;
import com.dwarfeng.springtelqos.sdk.util.CliCommandUtil;
import com.dwarfeng.springtelqos.stack.command.CommandDescriptor;
import com.dwarfeng.springtelqos.stack.command.CommandExecutor;
import com.dwarfeng.springterminator.stack.service.TerminateQosService;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 关闭/重启程序命令。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
@TelqosCommand
public class ShutdownCommand extends CliCommand {

    @SuppressWarnings({"SpellCheckingInspection", "GrazieInspectionRunner", "RedundantSuppression"})
    private static final String IDENTITY = "shutdown";

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownCommand.class);

    // region 指令选项

    private static final String COMMAND_OPTION_SHUTDOWN = "s";
    private static final String COMMAND_OPTION_RESTART = "r";

    private static final String[] COMMAND_OPTION_ARRAY = new String[]{
            COMMAND_OPTION_SHUTDOWN,
            COMMAND_OPTION_RESTART
    };

    private static final String COMMAND_SUB_OPTION_EXIT_CODE = "e";
    private static final String COMMAND_SUB_OPTION_COMMENT = "c";

    // endregion

    private final TerminateQosService terminateQosService;

    public ShutdownCommand(TerminateQosService terminateQosService) {
        super(IDENTITY);
        this.terminateQosService = terminateQosService;
    }

    @Override
    protected DescriptionProvider provideDescriptionProvider() {
        return ctx -> "关闭/重启程序";
    }

    @Override
    protected CliSyntaxProvider provideCliSyntaxProvider() {
        return this::cliSyntaxProvider;
    }

    private String cliSyntaxProvider(CommandDescriptor.Context context) throws Exception {
        final String[] patterns = new String[]{
                context.getRuntimeIdentity() + " [" +
                        CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_SHUTDOWN) + "/" +
                        CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_RESTART) + "] [" +
                        CliCommandUtil.concatOptionPrefix(COMMAND_SUB_OPTION_EXIT_CODE) + " exit-code] [" +
                        CliCommandUtil.concatOptionPrefix(COMMAND_SUB_OPTION_COMMENT) + " comment]"
        };
        return CliCommandUtil.cliSyntax(patterns);
    }

    @Override
    protected List<Option> provideOptions() {
        List<Option> list = new ArrayList<>();
        list.add(Option.builder(COMMAND_SUB_OPTION_EXIT_CODE).optionalArg(true).type(Number.class).hasArg(true)
                .argName("exit-code").desc("退出代码").get());
        list.add(Option.builder(COMMAND_SUB_OPTION_COMMENT).optionalArg(true).type(String.class).hasArg(true)
                .argName("comment").desc("备注").get());
        list.add(Option.builder(COMMAND_OPTION_SHUTDOWN).optionalArg(true).desc("退出程序").get());
        list.add(Option.builder(COMMAND_OPTION_RESTART).optionalArg(true).desc("重启程序").get());
        return list;
    }

    @Override
    protected void executeWithCmd(CommandExecutor.Context context, CommandLine cmd) throws Exception {
        Pair<String, Integer> pair = CliCommandUtil.analyseCommand(cmd, COMMAND_OPTION_ARRAY);
        if (pair.getRight() > 1) {
            context.sendMessage(CliCommandUtil.optionMismatchMessage(COMMAND_OPTION_ARRAY));
            context.sendMessage(context.getCommandManual(context.getRuntimeIdentity()));
            return;
        }

        // 解析参数。
        int exitCode = 0;
        String comment = null;
        boolean restartFlag = false;
        if (cmd.hasOption(COMMAND_SUB_OPTION_EXIT_CODE)) {
            exitCode = ((Number) cmd.getParsedOptionValue(COMMAND_SUB_OPTION_EXIT_CODE)).intValue();
        }
        if (cmd.hasOption(COMMAND_SUB_OPTION_COMMENT)) {
            comment = cmd.getParsedOptionValue(COMMAND_SUB_OPTION_COMMENT);
        }
        if (cmd.hasOption(COMMAND_OPTION_RESTART)) {
            restartFlag = true;
        }

        // 二次确认。
        boolean confirmFlag;
        a:
        do {
            context.sendMessage("服务将会关闭，您可能需要登录远程主机才能重新启动该服务，是否继续? Y/N");
            String confirmMessage = context.receiveMessage();
            switch (StringUtils.upperCase(confirmMessage)) {
                case "Y":
                    confirmFlag = true;
                    break a;
                case "N":
                    confirmFlag = false;
                    break a;
                default:
                    context.sendMessage("输入信息非法，请输入 Y 或者 N");
                    break;
            }
        } while (true);

        // 判断确认结果以及执行关闭动作。
        if (confirmFlag) {
            context.sendMessage("已确认请求，服务即将关闭...");
            if (StringUtils.isEmpty(comment)) {
                LOGGER.warn("设备 {} 通过 QOS 系统关闭了该服务，退出代码设置为 {}，备注未填",
                        context.getAddress(), exitCode);
            } else {
                LOGGER.warn("设备 {} 通过 QOS 系统关闭了该服务，退出代码设置为 {}，备注为 {}",
                        context.getAddress(), exitCode, comment);
            }
            context.quit();
            // 根据标记退出或重启程序。
            if (restartFlag) {
                terminateQosService.exitAndRestart(exitCode);
            } else {
                terminateQosService.exit(exitCode);
            }
        } else {
            context.sendMessage("已确认请求，服务不会不关闭");
        }
    }
}
