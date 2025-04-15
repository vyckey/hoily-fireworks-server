package com.hoily.service.fireworks.acl.wechat.security;

import lombok.Data;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/10 13:27
 */
@Data
public class EncryptionConfig {
    private final String token;
    private final String encodingAESKey;

    public EncryptionConfig(String token, String encodingAESKey) {
        this.token = token;
        this.encodingAESKey = encodingAESKey;
    }
}
