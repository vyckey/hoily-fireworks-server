package com.hoily.service.fireworks.application.wechat;

import com.google.common.collect.Lists;
import com.hoily.service.fireworks.acl.wechat.WechatApiClient;
import com.hoily.service.fireworks.acl.wechat.message.OfficialMessageDTO;
import com.hoily.service.fireworks.acl.wechat.message.OfficialTextMessageDTO;
import com.hoily.service.fireworks.acl.wechat.message.UserMessageDTO;
import com.hoily.service.fireworks.acl.wechat.message.UserTextMessageDTO;
import com.hoily.service.fireworks.application.wechat.chat.MessageHelper;
import com.hoily.service.fireworks.application.wechat.command.CommandLineExecutor;
import com.hoily.service.fireworks.application.wechat.command.HoilyCommand;
import com.hoily.service.fireworks.application.wechat.model.UserModelConfig;
import com.hoily.service.fireworks.application.wechat.model.WechatModelService;
import com.hoily.service.fireworks.infrastructure.common.utils.JsonObjectMapper;
import com.hoily.service.fireworks.infrastructure.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Wechat chat service
 *
 * @author vyckey
 * 2023/2/12 20:24
 */
@Slf4j
@Component
public class WechatChatService {
    private final WechatApiClient wechatApiClient;
    private final WechatModelService wechatModelService;
    private final OpenAiApi openAiApi;
    private final CommandLineExecutor commandLineExecutor;
    private final StringRedisTemplate redisTemplate;

    public WechatChatService(@Autowired(required = false) WechatApiClient wechatApiClient,
                             WechatModelService wechatModelService,
                             OpenAiApi openAiApi,
                             CommandLineExecutor commandLineExecutor,
                             StringRedisTemplate redisTemplate) {
        this.wechatApiClient = wechatApiClient;
        this.wechatModelService = wechatModelService;
        this.openAiApi = openAiApi;
        this.commandLineExecutor = commandLineExecutor;
        this.redisTemplate = redisTemplate;
    }

    public void storeMessages(String userName, List<Message> messages) {
        String redisKey = "wechat:chat_history:" + userName;
        String value = JsonObjectMapper.CAMEL_CASE.toJson(messages);
        redisTemplate.opsForValue().set(redisKey, value, 1, TimeUnit.HOURS);
    }

    public List<Message> retrieveMessages(String userName) {
        String redisKey = "wechat:chat_history:" + userName;
        String value = redisTemplate.opsForValue().get(redisKey);
        if (value == null) {
            return Lists.newArrayList();
        }
        return MessageHelper.fromJson(value);
    }

    public void clearSession(String userName) {
        String redisKey = "wechat:chat_history:" + userName;
        redisTemplate.delete(redisKey);
        log.info("clear chat session for {}", userName);
    }

    public AssistantMessage replyUserMessage(ChatModel chatModel, UserModelConfig modelConfig,
                                             String userName, Message message) {
        List<Message> messages = retrieveMessages(userName);
        if (StringUtils.isNotEmpty(modelConfig.getInstruction())) {
            messages.add(0, new SystemMessage(modelConfig.getInstruction()));
        }
        messages.add(message);

        ChatResponse chatResponse = chatModel.call(new Prompt(messages));
        Generation generation = chatResponse.getResult();
        AssistantMessage outputMessage = generation.getOutput();

        if (StringUtils.isNotEmpty(modelConfig.getInstruction())) {
            messages.remove(0);
        }
        messages.add(outputMessage);
        storeMessages(userName, messages);
        return outputMessage;
    }

    public OfficialMessageDTO replyUserMessage(UserTextMessageDTO userMessage) {
        String userName = userMessage.getFromUserName();
        UserModelConfig modelConfig = wechatModelService.queryModelConfig(userName);

        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(modelConfig.getModelName())
                        .build())
                .build();

        String outputContent;
        try {
            UserMessage message = new UserMessage(userMessage.getContent());
            AssistantMessage outputMessage = replyUserMessage(chatModel, modelConfig,
                    userMessage.getFromUserName(), message);
            outputContent = outputMessage.getText();
        } catch (Exception e) {
            log.error("Failed to reply user message caused by {}", e.getMessage(), e);
            outputContent = "不要意思，暂时回答不了你的问题哦~请联系管理员微信号\"vyckey0213\"！";
        }

        OfficialTextMessageDTO officialMessage = new OfficialTextMessageDTO();
        officialMessage.setContent(outputContent);
        officialMessage.setUserName(userMessage.getToUserName(), userMessage.getFromUserName());
        officialMessage.setCreateTime(System.currentTimeMillis() / 1000L);
        return officialMessage;
    }

    public OfficialMessageDTO handleMessage(UserMessageDTO userMessage) {
        if (userMessage instanceof UserTextMessageDTO) {
            UserTextMessageDTO textMessageDTO = (UserTextMessageDTO) userMessage;
            if (HoilyCommand.isCommand(textMessageDTO.getContent())) {
                String responseText = commandLineExecutor.executeChatCommand(
                        textMessageDTO.getFromUserName(), textMessageDTO.getContent());

                OfficialTextMessageDTO officialMessage = new OfficialTextMessageDTO();
                officialMessage.setContent(responseText);
                officialMessage.setUserName(userMessage.getToUserName(), userMessage.getFromUserName());
                officialMessage.setCreateTime(System.currentTimeMillis() / 1000L);
                return officialMessage;
            } else {
                return replyUserMessage(textMessageDTO);
            }
        } else {
            OfficialTextMessageDTO officialMessage = new OfficialTextMessageDTO();
            officialMessage.setContent("暂时不支持的消息类型");
            officialMessage.setUserName(userMessage.getToUserName(), userMessage.getFromUserName());
            officialMessage.setCreateTime(System.currentTimeMillis() / 1000L);
            return officialMessage;
        }
    }
}
