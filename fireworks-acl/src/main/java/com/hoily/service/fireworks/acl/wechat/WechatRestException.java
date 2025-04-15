package com.hoily.service.fireworks.acl.wechat;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/10 10:00
 */
public class WechatRestException extends RuntimeException {
    public WechatRestException(String message) {
        super(message);
    }

    public WechatRestException(String message, Throwable cause) {
        super(message, cause);
    }
}
