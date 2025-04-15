package com.hoily.service.fireworks.acl.wechat.security;

import lombok.Data;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/10 13:53
 */
@Data
public class AccessToken {
    private final String accessToken;
    private final Date expiredAt;

    public AccessToken(String accessToken, int expiredAfterSecond) {
        this(accessToken, DateUtils.addSeconds(new Date(), expiredAfterSecond));
    }

    public AccessToken(String accessToken, Date expiredAt) {
        this.accessToken = accessToken;
        this.expiredAt = expiredAt;
    }

    public boolean isExpired() {
        return expiredAt.before(new Date());
    }
}
