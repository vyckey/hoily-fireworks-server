package com.hoily.service.fireworks.acl.wechat.customer.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.Maps;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Service_Center_messages.html#%E5%AE%A2%E6%9C%8D%E6%8E%A5%E5%8F%A3-%E5%8F%91%E6%B6%88%E6%81%AF">客服发消息</a>
 *
 * @author vyckey
 * 2023/3/7 19:09
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "msgType",
        defaultImpl = CustomerMessageBaseDTO.class,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CustomerTextMessageDTO.class, name = CustomerTextMessageDTO.MSG_TYPE),
        @JsonSubTypes.Type(value = CustomerImageMessageDTO.class, name = CustomerImageMessageDTO.MSG_TYPE),
        @JsonSubTypes.Type(value = CustomerVoiceMessageDTO.class, name = CustomerVoiceMessageDTO.MSG_TYPE),
        @JsonSubTypes.Type(value = CustomerVideoMessageDTO.class, name = CustomerVideoMessageDTO.MSG_TYPE),
        @JsonSubTypes.Type(value = CustomerMusicMessageDTO.class, name = CustomerMusicMessageDTO.MSG_TYPE),
})
@Data
public class CustomerMessageBaseDTO implements Serializable {
    private String msgType;

    public Map<String, Object> wrapAsMessage(String toUserId) {
        Map<String, Object> message = Maps.newHashMapWithExpectedSize(3);
        message.put("touser", toUserId);
        message.put("msgtype", getMsgType());
        message.put(getMsgType(), this);
        return message;
    }
}
