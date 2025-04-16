package com.hoily.service.fireworks.acl.wechat;

import com.hoily.service.fireworks.acl.wechat.security.WechatEncryption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description is here
 *
 * @author vyckey
 */
@ConditionalOnProperty(name = "wechat.enabled", havingValue = "true")
@Configuration
public class WechatConfiguration {
    @Bean
    public WechatEncryption wechatEncryption(@Value("${wechat.authentication.token}") String token,
                                             @Value("${wechat.authentication.encodingAESKey}") String encodingAESKey) {
        return new WechatEncryption(token, encodingAESKey);
    }

    @Bean
    public WechatApiClient wechatApiClient() {
        return new WechatApiClient();
    }

    @Bean
    public WechatTokenHelper wechatTokenHelper(WechatApiClient wechatApiClient,
                                               @Value("${wechat.app.id}") String appId,
                                               @Value("${wechat.app.secret}") String appSecret) {
        return new WechatTokenHelper(wechatApiClient, appId, appSecret);
    }
}
