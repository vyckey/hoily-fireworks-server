package com.hoily.service.fireworks.acl.wechat.customer.message;

import lombok.Data;

import java.io.Serializable;

/**
 * description is here
 *
 * @author vyckey
 * 2023/3/7 19:18
 */
@Data
public class MessageTypingRequest implements Serializable {
    private String toUserId;

    private String command;

    protected MessageTypingRequest(String toUserId, String command) {
        this.toUserId = toUserId;
        this.command = command;
    }

    public static MessageTypingRequest typing(String toUserId) {
        return new MessageTypingRequest(toUserId, "Typing");
    }

    public static MessageTypingRequest cancel(String toUserId) {
        return new MessageTypingRequest(toUserId, "CancelTyping");
    }
}
