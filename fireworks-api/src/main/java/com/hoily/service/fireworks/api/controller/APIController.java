package com.hoily.service.fireworks.api.controller;

import com.google.common.collect.Maps;
import com.hoily.service.fireworks.contract.response.BaseResponse;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/14 13:31
 */
@Slf4j
@RestController
@RequestMapping("api")
@AllArgsConstructor
public class APIController {
    private Environment environment;

    @PostConstruct
    public void initialize() {
        List<String> keys = Arrays.asList("openai.authentication.api_key", "wechat.app.id", "wechat.authentication.token");
        for (String key : keys) {
            log.info("appenv {}={}", key, environment.getProperty(key));
        }
    }

    @GetMapping("")
    @ResponseBody
    public BaseResponse<?> abstractInfo() {
        Map<String, Object> infoMap = Maps.newHashMap();
        infoMap.put("author", "vyckey");
        infoMap.put("server", "fireworks-api");
        infoMap.put("time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return BaseResponse.success(infoMap).build();
    }

}
