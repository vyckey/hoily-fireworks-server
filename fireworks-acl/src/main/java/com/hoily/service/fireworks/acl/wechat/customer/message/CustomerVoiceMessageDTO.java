package com.hoily.service.fireworks.acl.wechat.customer.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * description is here
 *
 * @author vyckey
 * 2023/3/7 19:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerVoiceMessageDTO extends CustomerMessageBaseDTO {
    public static final String MSG_TYPE = "voice";

    @JsonProperty("media_id")
    private String mediaId;

    @JsonCreator
    public CustomerVoiceMessageDTO(@JsonProperty("media_id") String mediaId) {
        this.mediaId = mediaId;
    }

    @Override
    public String getMsgType() {
        return MSG_TYPE;
    }
}
