package com.hoily.service.fireworks.acl.wechat.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/10 10:17
 */
@Data
public class AccessTokenDTO implements Serializable {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiredAfter;
}
