package com.hoily.service.fireworks.acl.wechat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hoily.service.fireworks.acl.wechat.base.WechatResponse;
import com.hoily.service.fireworks.acl.wechat.customer.message.MessageTypingRequest;
import com.hoily.service.fireworks.acl.wechat.security.AccessTokenDTO;
import com.hoily.service.fireworks.acl.wechat.security.AppConfig;
import com.hoily.service.fireworks.infrastructure.common.utils.JsonObjectMapper;
import com.hoily.service.fireworks.infrastructure.common.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.Collections;

/**
 * Wechat rest template
 *
 * @author vyckey
 * 2023/2/10 09:32
 */
@Slf4j
@Component
public class WechatRestTemplate {
    private static final String WECHAT_DOMAIN = "https://api.weixin.qq.com";
    private final HttpServletRequest request;
    private final RestTemplate restTemplate;
    private WechatAuthenticationManager authenticationManager;

    public WechatRestTemplate(HttpServletRequest request, RestTemplate restTemplate) {
        this.request = request;
        this.restTemplate = restTemplate;
    }

    @Lazy
    @Autowired
    public void setAuthenticationManager(WechatAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public <T> T exchange(String uri, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(WECHAT_DOMAIN + "/" + uri, method, requestEntity, responseType, uriVariables);
            if (log.isDebugEnabled()) {
                log.debug("doExchange: call service with request {} and response {}",
                        JsonObjectMapper.CAMEL_CASE.toJson(request), JsonObjectMapper.CAMEL_CASE.toJson(response));
            }
            return response.getBody();
        } catch (HttpStatusCodeException ce) {
            throw new WechatRestException("[" + uri + "]: http status code: " + ce.getStatusText());
        } catch (RestClientException rce) {
            throw new WechatRestException("[" + uri + "]: rest ex", rce);
        } catch (Exception e) {
            throw new WechatRestException("[" + uri + "]: internal ex", e);
        }
    }

    public <T> T exchange(String uri, HttpMethod method, Object request, TypeReference<T> responseType, Object... uriVariables) {
        String requestBody = (request != null) ? JsonObjectMapper.CAMEL_CASE.toJson(request) : null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
        T response = exchange(uri, method, httpEntity, new ParameterizedTypeReference<T>() {
            @Override
            public Type getType() {
                return responseType.getType();
            }
        }, uriVariables);

        WechatResponse<?> resp;
        if (response instanceof WechatResponse && !(resp = (WechatResponse<?>) response).isSuccess()) {
            log.warn("wechat [{}]: errcode:{}, errmsg:{}", uri, resp.getErrorCode(), resp.getErrorMsg());
        }
        return response;
    }

    public WechatResponse<AccessTokenDTO> requestAccessToken() {
        AppConfig appConfig = authenticationManager.getAppConfig();
        return exchange("cgi-bin/token?grant_type=client_credential&appid={appId}&secret={appSecret}",
                HttpMethod.GET, null, new TypeReference<WechatResponse<AccessTokenDTO>>() {
                }, appConfig.getAppId(), appConfig.getAppSecret());
    }

    private String getAccessToken() {
        String accessToken = authenticationManager.getOrRefreshAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            throw new WechatRestException("fail to refresh access_token");
        }
        return accessToken;
    }

    public WechatResponse<Void> sendCustomerMessage(Object customerMessage) {
        return exchange("cgi-bin/message/custom/send?access_token={accessToken}",
                HttpMethod.POST, customerMessage, new TypeReference<WechatResponse<Void>>() {
                }, getAccessToken());
    }

    public WechatResponse<Void> sendMessageTyping(MessageTypingRequest request) {
        return exchange("cgi-bin/message/custom/typing?access_token={accessToken}",
                HttpMethod.POST, request, new TypeReference<WechatResponse<Void>>() {
                }, getAccessToken());
    }
}
