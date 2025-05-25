package com.hoily.service.fireworks.application.wechat.chat;

import lombok.Getter;

@Getter
public class ChatContext {
    private final String userName;

    public ChatContext(String userName) {
        this.userName = userName;
    }
}
