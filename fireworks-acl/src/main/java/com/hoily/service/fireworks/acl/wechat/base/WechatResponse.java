package com.hoily.service.fireworks.acl.wechat.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

import java.io.Serializable;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/10 10:11
 */
@Data
public class WechatResponse<T> implements Serializable {
    public static final Integer CODE_SUCCESS = 0;

    @JsonProperty("errcode")
    private Integer errorCode;

    @JsonProperty("errmsg")
    private String errorMsg;

    @JsonUnwrapped
    private T result;

    public boolean isSuccess() {
        return errorCode == null || CODE_SUCCESS.equals(errorCode);
    }

    public static <T> WechatResponse<T> success(T result) {
        WechatResponse<T> response = new WechatResponse<>();
        response.setResult(result);
        return response;
    }
}
