package com.hoily.service.fireworks.acl.wechat;

import com.google.common.collect.Lists;
import com.hoily.service.fireworks.acl.wechat.base.WechatResponse;
import com.hoily.service.fireworks.acl.wechat.security.AccessToken;
import com.hoily.service.fireworks.acl.wechat.security.AccessTokenDTO;
import com.hoily.service.fireworks.acl.wechat.security.AppConfig;
import com.hoily.service.fireworks.acl.wechat.security.EncryptionConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/10 09:58
 */
@Slf4j
@Component
public class WechatAuthenticationManager {
    private final WechatRestTemplate wechatRestTemplate;
    private final AtomicReference<AccessToken> accessTokenHolder = new AtomicReference<>();
    private AppConfig appConfig;
    private EncryptionConfig encryptionConfig;

    public WechatAuthenticationManager(WechatRestTemplate wechatRestTemplate) {
        this.wechatRestTemplate = wechatRestTemplate;
    }

    AppConfig getAppConfig() {
        return appConfig;
    }

    @Autowired
    public void setAppConfig(@Value("${wechat.app.id}") String appId, @Value("${wechat.app.secret}") String appSecret) {
        this.appConfig = new AppConfig(appId, appSecret);
    }

    private EncryptionConfig getEncryptionConfig() {
        return encryptionConfig;
    }

    @Autowired
    public void setEncryptionConfig(@Value("${wechat.authentication.token}") String token,
                                    @Value("${wechat.authentication.encodingAESKey}") String encodingAESKey) {
        this.encryptionConfig = new EncryptionConfig(token, encodingAESKey);
    }

    /**
     * refer to <a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html">微信开发文档 - 接入指南</a>
     */
    public boolean signatureValid(String timestamp, String nonce, String signature) {
        String token = getEncryptionConfig().getToken();
        String joinStr = Lists.newArrayList(token, timestamp, nonce).stream()
                .map(String::valueOf).sorted().collect(Collectors.joining(""));
        return DigestUtils.sha1Hex(joinStr).equals(signature);
    }


    public String getOrRefreshAccessToken() {
        AccessToken accessToken = accessTokenHolder.get();
        if (accessToken == null || accessToken.isExpired()) {
            WechatResponse<AccessTokenDTO> response = wechatRestTemplate.requestAccessToken();
            if (response.isSuccess() && response.getResult() != null) {
                AccessTokenDTO result = response.getResult();
                accessToken = new AccessToken(result.getAccessToken(), result.getExpiredAfter());
            } else {
                accessToken = null;
            }
            accessTokenHolder.set(accessToken);
        }
        return accessToken != null ? accessToken.getAccessToken() : null;
    }
}
