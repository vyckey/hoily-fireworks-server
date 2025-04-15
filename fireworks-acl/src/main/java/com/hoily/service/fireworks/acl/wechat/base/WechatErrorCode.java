package com.hoily.service.fireworks.acl.wechat.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <a href="https://developers.weixin.qq.com/doc/offiaccount/Getting_Started/Global_Return_Code.html">Wechat error code</a>
 *
 * @author vyckey
 * 2023/3/7 19:47
 */
@Getter
@AllArgsConstructor
public enum WechatErrorCode {
    SYSTEM_BUSY(-1, "系统繁忙，此时请开发者稍候再试"),
    SUCCESS(0, "请求成功"),
    APP_SECRET_INVALID(0, "获取 access_token 时 AppSecret 错误，或者 access_token 无效"),
    ILLEGAL_CERTIFICATE(40002, "不合法的凭证类型"),
    ILLEGAL_OPENID(40003, "不合法的OpenID"),
    INVALID_ACCESS_TOKEN(41001, "缺少access_token参数"),
    INVALID_APPID(41002, "缺少appid参数"),
    INVALID_SECRET(41004, "缺少secret参数"),
    ;

    private final Integer code;
    private final String name;

    public static WechatErrorCode of(Integer code) {
        for (WechatErrorCode errorCode : values()) {
            if (errorCode.code.equals(code)) {
                return errorCode;
            }
        }
        return null;
    }
}
