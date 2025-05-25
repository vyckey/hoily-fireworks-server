package com.hoily.service.fireworks.server.controller;

import com.hoily.service.fireworks.acl.wechat.base.XmlWrapper;
import com.hoily.service.fireworks.acl.wechat.message.OfficialMessageDTO;
import com.hoily.service.fireworks.acl.wechat.message.UserMessageDTO;
import com.hoily.service.fireworks.acl.wechat.security.WechatEncryption;
import com.hoily.service.fireworks.application.wechat.WechatChatService;
import com.hoily.service.fireworks.infrastructure.common.utils.JsonObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Wechat controller
 *
 * @author vyckey
 * 2023/2/10 08:08
 */
@Slf4j
@RestController
@RequestMapping("api/wechat")
@AllArgsConstructor
public class WechatController {
    private final WechatEncryption wechatEncryption;
    private final WechatChatService wechatChatService;

    @GetMapping("callback")
    @ResponseBody
    public String checkSignature(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp,
                                 @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) {
        log.info("wechat access validate... {}", timestamp);
        if (wechatEncryption.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "invalid signature";
    }

    @PostMapping(value = "callback", consumes = {MediaType.TEXT_XML_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.TEXT_XML_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public XmlWrapper<OfficialMessageDTO> callback(@RequestBody XmlWrapper<UserMessageDTO> request) {
        UserMessageDTO userMessage = request.getObject();
        log.info("wechat callback message:{}", JsonObjectMapper.CAMEL_CASE.toJson(userMessage));
        OfficialMessageDTO officialMessage = wechatChatService.handleMessage(userMessage);
        log.info("wechat callback reply:{} => {}", userMessage.getFromUserName(), JsonObjectMapper.CAMEL_CASE.toJson(officialMessage));
        return officialMessage != null ? XmlWrapper.of(officialMessage) : null;
    }
}
