package com.hoily.service.fireworks.application.wechat.command;

import com.hoily.service.fireworks.application.wechat.WechatChatService;
import com.hoily.service.fireworks.application.wechat.chat.ChatContext;
import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * Chat session command
 *
 * @author vyckey
 */
@CommandLine.Command(name = "session", description = "chat session command", mixinStandardHelpOptions = true)
public class ChatSessionCommand implements Callable<String> {
    private final ChatContext chatContext;
    private final WechatChatService wechatChatService;

    @CommandLine.Option(names = {"--clear"}, description = "clear chat session")
    private boolean clear;

    public ChatSessionCommand(ChatContext chatContext, WechatChatService wechatChatService) {
        this.chatContext = chatContext;
        this.wechatChatService = wechatChatService;
    }

    @Override
    public String call() throws Exception {
        if (clear) {
            wechatChatService.clearSession(chatContext.getUserName());
            return "Chat session cleared.";
        }
        return null;
    }

}