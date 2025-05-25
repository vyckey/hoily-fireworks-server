package com.hoily.service.fireworks.application.wechat.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * User model config
 *
 * @author vyckey
 */
@Getter
@Setter
public class UserModelConfig {
    private final String userName;
    private String modelName = "deepseek-r1";
    private String instruction;

    @JsonCreator
    public UserModelConfig(@JsonProperty("userName") String userName) {
        this.userName = Objects.requireNonNull(userName);
    }
}
