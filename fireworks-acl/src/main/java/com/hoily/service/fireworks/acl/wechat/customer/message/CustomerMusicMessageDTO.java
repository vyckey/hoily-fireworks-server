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
public class CustomerMusicMessageDTO extends CustomerMessageBaseDTO {
    public static final String MSG_TYPE = "music";

    private String title;

    private String description;

    @JsonProperty("music_url")
    private String musicUrl;

    @JsonProperty("hp_music_url")
    private String hpMusicUrl;

    @JsonProperty("thumb_media_id")
    private String thumbMediaId;

    @Override
    public String getMsgType() {
        return MSG_TYPE;
    }
}
