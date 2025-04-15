package com.hoily.service.fireworks.acl.wechat;

import com.hoily.service.fireworks.acl.AclContext;
import com.hoily.service.fireworks.acl.wechat.base.WechatResponse;
import com.hoily.service.fireworks.acl.wechat.security.AccessTokenDTO;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/12 20:16
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AclContext.class)
@SpringBootTest
public class WechatRestTemplateTest {
    @Autowired
    private WechatAuthenticationManager wechatAuthenticationManager;
    @Autowired
    private WechatRestTemplate wechatRestTemplate;

    @Test
    public void accessTokenTest() {
        WechatResponse<AccessTokenDTO> response = wechatRestTemplate.requestAccessToken();
        Assert.assertTrue(response.isSuccess());
    }
}