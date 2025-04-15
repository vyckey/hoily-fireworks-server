package com.hoily.service.fireworks.acl.wechat.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Wechat user message dto
 *
 * <p>refer to <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html">微信开发文档 - 接收普通消息</a></p>
 *
 * @author vyckey
 * 2023/2/9 21:13
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "MsgType",
        defaultImpl = UserMessageDTO.class,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserEventMessageDTO.class, name = UserEventMessageDTO.MSG_TYPE),
        @JsonSubTypes.Type(value = UserTextMessageDTO.class, name = UserTextMessageDTO.MSG_TYPE),
        @JsonSubTypes.Type(value = UserImageMessageDTO.class, name = UserImageMessageDTO.MSG_TYPE),
        @JsonSubTypes.Type(value = UserLinkMessageDTO.class, name = UserLinkMessageDTO.MSG_TYPE),
})
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserMessageDTO extends BaseMessageDTO {
    protected Long msgId;

    protected String msgDataId;

    protected Integer idx;
}
