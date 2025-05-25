package com.hoily.service.fireworks.application.wechat.model;

import com.hoily.service.fireworks.infrastructure.common.utils.JsonObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Wechat model service
 *
 * @author vyckey
 */
@Slf4j
@AllArgsConstructor
@Service
public class WechatModelService {
    private final StringRedisTemplate redisTemplate;

    public UserModelConfig queryModelConfig(String userName) {
        String redisKey = "wechat:model_config:" + userName;
        String value = redisTemplate.opsForValue().get(redisKey);
        if (value != null) {
            return JsonObjectMapper.CAMEL_CASE.fromJson(value, UserModelConfig.class);
        }
        return new UserModelConfig(userName);
    }

    public void saveModelConfig(UserModelConfig modelConfig) {
        String redisKey = "wechat:model_config:" + modelConfig.getUserName();
        String value = JsonObjectMapper.CAMEL_CASE.toJson(modelConfig);
        redisTemplate.opsForValue().set(redisKey, value, 7, TimeUnit.DAYS);
    }
}
