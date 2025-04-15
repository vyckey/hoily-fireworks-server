package com.hoily.service.fireworks.acl.wechat.message;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Wechat basic message dto
 *
 * @author vyckey
 * 2023/2/9 21:13
 */
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Data
public class BaseMessageDTO implements Serializable {
    protected String fromUserName;

    protected String toUserName;

    protected String msgType;

    protected Long createTime;

    public void setUserName(String fromUserName, String toUserName) {
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
    }

    public boolean isMsgType(String msgType) {
        return Objects.equals(this.msgType, msgType);
    }

}
