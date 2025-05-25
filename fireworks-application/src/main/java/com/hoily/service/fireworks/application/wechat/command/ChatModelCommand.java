package com.hoily.service.fireworks.application.wechat.command;

import com.hoily.service.fireworks.application.wechat.chat.ChatContext;
import com.hoily.service.fireworks.application.wechat.model.UserModelConfig;
import com.hoily.service.fireworks.application.wechat.model.WechatModelService;
import com.hoily.service.fireworks.infrastructure.common.utils.StringUtils;
import picocli.CommandLine;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Chat model command
 *
 * @author vyckey
 */
@CommandLine.Command(name = "model", description = "chat model command", mixinStandardHelpOptions = true)
public class ChatModelCommand implements Callable<String> {
    private final ChatContext chatContext;
    private final WechatModelService wechatModelService;

    @CommandLine.Option(names = {"--name"}, description = "set model name")
    private String name;

    @CommandLine.Option(names = {"--instruction"}, description = "set model instruction")
    private String instruction;

    public ChatModelCommand(ChatContext chatContext, WechatModelService wechatModelService) {
        this.chatContext = chatContext;
        this.wechatModelService = wechatModelService;
    }

    @Override
    public String call() throws Exception {
        UserModelConfig modelConfig = wechatModelService.queryModelConfig(chatContext.getUserName());
        if (StringUtils.isNotEmpty(name) || StringUtils.isNotEmpty(instruction)) {
            Optional.ofNullable(name).ifPresent(modelConfig::setModelName);
            Optional.ofNullable(instruction).ifPresent(modelConfig::setInstruction);
            wechatModelService.saveModelConfig(modelConfig);
            return "Model configuration is updated successfully.";
        }
        return null;
    }

}
