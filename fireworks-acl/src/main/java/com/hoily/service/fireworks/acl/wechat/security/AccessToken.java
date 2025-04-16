package com.hoily.service.fireworks.acl.wechat.security;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * Access Token
 *
 * @author vyckey
 */
public record AccessToken(String token, Date expiredAt) {
    public AccessToken(String token, int expiredSeconds) {
        this(token, DateUtils.addSeconds(new Date(), expiredSeconds));
    }

    public boolean isExpired() {
        return expiredAt.before(new Date());
    }
}
