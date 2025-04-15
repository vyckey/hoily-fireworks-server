package com.hoily.service.fireworks.acl.wechat.security;

import lombok.Data;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/10 10:16
 */
@Data
public class AppConfig {
    private final String appId;
    private final String appSecret;

    public AppConfig(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }
}
