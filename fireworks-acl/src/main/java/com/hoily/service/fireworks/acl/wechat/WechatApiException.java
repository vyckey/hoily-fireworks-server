package com.hoily.service.fireworks.acl.wechat;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/10 10:00
 */
public class WechatApiException extends RuntimeException {
    public WechatApiException(String message) {
        super(message);
    }

    public WechatApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
