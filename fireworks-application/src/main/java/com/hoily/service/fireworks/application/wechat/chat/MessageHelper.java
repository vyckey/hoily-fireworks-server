package com.hoily.service.fireworks.application.wechat.chat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hoily.service.fireworks.infrastructure.common.utils.JsonObjectMapper;
import org.apache.commons.collections.MapUtils;
import org.springframework.ai.chat.messages.*;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class MessageHelper {
    public static List<Message> fromJson(String json) {
        List<Map<String, Object>> maps = JsonObjectMapper.CAMEL_CASE.fromJson(json, new TypeReference<>() {
        });
        return maps.stream().map(MessageHelper::fromMap).collect(Collectors.toList());
    }

    public static Message fromMap(Map<String, Object> map) {
        String messageTypeStr = MapUtils.getString(map, "messageType");
        MessageType messageType = MessageType.fromValue(messageTypeStr.toLowerCase());
        Message message;
        switch (messageType) {
            case SYSTEM:
                message = new SystemMessage(MapUtils.getString(map, "text"));
                message.getMetadata().putAll(MapUtils.getMap(map, "metadata"));
                break;
            case USER:
                message = new UserMessage(MapUtils.getString(map, "text"));
                BeanUtils.copyProperties(map, message);
                break;
            case ASSISTANT:
                message = new AssistantMessage(MapUtils.getString(map, "text"));
                BeanUtils.copyProperties(map, message);
                break;
            case TOOL:
                List<ToolResponseMessage.ToolResponse> responses = JsonObjectMapper.CAMEL_CASE.convert(
                        map.get("responses"), new TypeReference<>() {
                        });
                message = new ToolResponseMessage(responses);
                BeanUtils.copyProperties(map, message);
                break;
            default:
                throw new IllegalArgumentException("Unsupported message type: " + messageType);
        }
        return message;
    }
}
