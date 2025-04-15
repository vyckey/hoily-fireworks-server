package com.hoily.service.fireworks.acl.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/9 21:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OfficialTextMessageDTO extends OfficialMessageDTO {
    public static final String MSG_TYPE = "text";

    private String content;

    @Override
    public String getMsgType() {
        return MSG_TYPE;
    }
}
