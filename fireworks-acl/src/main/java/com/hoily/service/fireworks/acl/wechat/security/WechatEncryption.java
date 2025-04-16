package com.hoily.service.fireworks.acl.wechat.security;

import com.google.common.collect.Lists;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.stream.Collectors;

/**
 * Wechat encryption
 *
 * @author vyckey
 */
public record WechatEncryption(String token, String encodingAESKey) {

    public boolean checkSignature(String timestamp, String nonce, String signature) {
        String joinStr = Lists.newArrayList(token, timestamp, nonce).stream()
                .map(String::valueOf).sorted().collect(Collectors.joining(""));
        return DigestUtils.sha1Hex(joinStr).equals(signature);
    }
}
