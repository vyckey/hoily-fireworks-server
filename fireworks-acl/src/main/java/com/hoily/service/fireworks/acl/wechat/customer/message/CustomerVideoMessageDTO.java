package com.hoily.service.fireworks.acl.wechat.customer.message;

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
public class CustomerVideoMessageDTO extends CustomerMessageBaseDTO {
    public static final String MSG_TYPE = "video";

    private String title;

    private String description;

    @JsonProperty("media_id")
    private String mediaId;

    @JsonProperty("thumb_media_id")
    private String thumbMediaId;

    @Override
    public String getMsgType() {
        return MSG_TYPE;
    }
}
