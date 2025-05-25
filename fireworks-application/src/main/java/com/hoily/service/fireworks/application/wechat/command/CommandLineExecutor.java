package com.hoily.service.fireworks.application.wechat.command;

import com.google.common.base.Preconditions;
import com.hoily.service.fireworks.application.wechat.WechatChatService;
import com.hoily.service.fireworks.application.wechat.chat.ChatContext;
import com.hoily.service.fireworks.application.wechat.model.WechatModelService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * description is here
 *
 * @author vyckey
 */
@Service
public class CommandLineExecutor implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public String executeChatCommand(String userName, String command) {
        Preconditions.checkArgument(HoilyCommand.isCommand(command), "invalid command: %s", command);
        command = command.substring(HoilyCommand.HOILY_CMD.length()).trim();

        WechatChatService wechatChatService = applicationContext.getBean(WechatChatService.class);
        WechatModelService wechatModelService = applicationContext.getBean(WechatModelService.class);
        ChatContext chatContext = new ChatContext(userName);

        CommandLine commandLine = new CommandLine(new HoilyCommand())
                .addSubcommand(new ChatModelCommand(chatContext, wechatModelService))
                .addSubcommand(new ChatSessionCommand(chatContext, wechatChatService));
        CommandLineExceptionHandler exceptionHandler = new CommandLineExceptionHandler();
        commandLine.setParameterExceptionHandler(exceptionHandler)
                .setExecutionExceptionHandler(exceptionHandler);
        StringWriter stringWriter = new StringWriter();
        commandLine.setOut(new PrintWriter(stringWriter));

        String[] args = command.split("\\s+");
        int exitCode = commandLine.execute(args);
        if (exitCode != 0) {
            throw new IllegalStateException(exceptionHandler.getException());
        }
        Object result = getExecutionResult(commandLine);
        if (result != null) {
            return result.toString();
        }
        return stringWriter.toString();
    }

    private static Object getExecutionResult(CommandLine commandLine) {
        if (commandLine.getParseResult().hasSubcommand()) {
            String subcommand = commandLine.getParseResult().subcommand().commandSpec().name();
            CommandLine subCommandLine = commandLine.getSubcommands().get(subcommand);
            return subCommandLine.getExecutionResult();
        } else {
            return commandLine.getExecutionResult();
        }
    }
}
