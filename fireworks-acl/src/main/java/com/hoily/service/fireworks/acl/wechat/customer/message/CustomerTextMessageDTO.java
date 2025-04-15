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
public class CustomerTextMessageDTO extends CustomerMessageBaseDTO {
    public static final String MSG_TYPE = "text";

    private String content;

    @JsonCreator
    public CustomerTextMessageDTO(@JsonProperty("content") String content) {
        this.content = content;
    }

    @Override
    public String getMsgType() {
        return MSG_TYPE;
    }
}
