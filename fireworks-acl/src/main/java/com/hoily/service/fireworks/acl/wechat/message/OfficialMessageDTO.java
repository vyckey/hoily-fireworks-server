package com.hoily.service.fireworks.acl.wechat.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Wechat official account message dto
 *
 * <p>refer to <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Passive_user_reply_message.html">微信开发文档 - 被动回复消息</a></p>
 *
 * @author vyckey
 * 2023/2/9 21:13
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "MsgType",
        defaultImpl = OfficialMessageDTO.class,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OfficialTextMessageDTO.class, name = OfficialTextMessageDTO.MSG_TYPE),
        @JsonSubTypes.Type(value = OfficialImageMessageDTO.class, name = OfficialImageMessageDTO.MSG_TYPE),
})
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OfficialMessageDTO extends BaseMessageDTO {

}
