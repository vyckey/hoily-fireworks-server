package com.hoily.service.fireworks.acl.wechat.message;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

/**
 * Wechat user event message dto
 *
 * <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html">自定义菜单 - 事件推送</a>
 *
 * @author vyckey
 * 2023/2/15 19:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserEventMessageDTO extends UserMessageDTO {
    public static final String MSG_TYPE = "event";

    @JsonProperty("Event")
    private String eventType;

    private String eventKey;

    @JsonAnyGetter
    private final Map<String, Object> properties = Maps.newHashMap();

    @JsonAnySetter
    public void setProperty(String propKey, Object propValue) {
        properties.put(propKey, propValue);
    }

    @Override
    public String getMsgType() {
        return MSG_TYPE;
    }
}
