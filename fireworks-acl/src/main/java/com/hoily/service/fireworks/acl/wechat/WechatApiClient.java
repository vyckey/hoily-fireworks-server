package com.hoily.service.fireworks.acl.wechat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoily.service.fireworks.acl.wechat.base.WechatResponse;
import com.hoily.service.fireworks.acl.wechat.customer.message.MessageTypingRequest;
import com.hoily.service.fireworks.acl.wechat.security.AccessTokenDTO;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

public class WechatApiClient {
    private static final String WECHAT_BASE_URL = "https://api.weixin.qq.com";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(INDENT_OUTPUT);
    private final WechatApi wechatApi;

    public WechatApiClient(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WECHAT_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(OBJECT_MAPPER))
                .build();
        this.wechatApi = retrofit.create(WechatApi.class);
    }

    public WechatApiClient() {
        this(new OkHttpClient.Builder().build());
    }

    private <T> T getBody(Response<T> response) throws IOException {
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw toException(response);
        }
    }

    private static RuntimeException toException(Response<?> response) throws IOException {
        try (ResponseBody responseBody = response.errorBody()) {
            int code = response.code();
            if (responseBody != null) {
                String body = responseBody.string();
                String errorMessage = String.format("status:%s, body:%s", code, body);
                return new WechatApiException(errorMessage);
            } else {
                return new WechatApiException(String.format("status: %s", code));
            }
        }
    }

    public WechatResponse<AccessTokenDTO> requestToken(String appId, String secret) {
        try {
            Response<WechatResponse<AccessTokenDTO>> response =
                    wechatApi.requestToken("client_credential", appId, secret).execute();
            return getBody(response);
        } catch (IOException e) {
            throw new WechatApiException(e.getMessage(), e);
        }
    }

    public WechatResponse<Void> sendMessage(String accessToken, Object customerMessage) {
        try {
            Response<WechatResponse<Void>> response = wechatApi.sendMessage(accessToken, customerMessage).execute();
            return getBody(response);
        } catch (IOException e) {
            throw new WechatApiException(e.getMessage(), e);
        }
    }

    public WechatResponse<Void> sendTyping(String accessToken, MessageTypingRequest request) {
        try {
            Response<WechatResponse<Void>> response = wechatApi.sendTyping(accessToken, request).execute();
            return getBody(response);
        } catch (IOException e) {
            throw new WechatApiException(e.getMessage(), e);
        }
    }
}
