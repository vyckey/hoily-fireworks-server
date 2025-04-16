package com.hoily.service.fireworks.acl.wechat;

import com.hoily.service.fireworks.acl.wechat.base.WechatResponse;
import com.hoily.service.fireworks.acl.wechat.security.AccessToken;
import com.hoily.service.fireworks.acl.wechat.security.AccessTokenDTO;

import java.util.Objects;

/**
 * Wechat token helper
 *
 * @author vyckey
 */
public class WechatTokenHelper {
    private volatile AccessToken accessTokenHolder;
    private final WechatApiClient wechatApiClient;
    private final String appId;
    private final String appSecret;

    public WechatTokenHelper(WechatApiClient wechatApiClient, String appId, String appSecret) {
        this.wechatApiClient = Objects.requireNonNull(wechatApiClient);
        this.appId = Objects.requireNonNull(appId, "wechat appid is required");
        this.appSecret = Objects.requireNonNull(appSecret, "wechat secret is required");
    }

    public String getOrRefreshToken() {
        AccessToken accessToken = accessTokenHolder;
        if (accessToken == null || accessToken.isExpired()) {
            synchronized (this) {
                accessToken = accessTokenHolder;
                if (accessToken != null && !accessToken.isExpired()) {
                    return accessToken.token();
                }

                WechatResponse<AccessTokenDTO> response = wechatApiClient.requestToken(appId, appSecret);
                if (response.isSuccess()) {
                    AccessTokenDTO result = response.getResult();
                    accessTokenHolder = new AccessToken(result.getAccessToken(), result.getExpiredAfter());
                    accessToken = accessTokenHolder;
                } else {
                    throw new WechatApiException("Failed to get access token: " + response.getErrorMsg());
                }
            }
        }
        return accessToken.token();
    }
}
