package com.hoily.service.fireworks.server.controller;

import com.hoily.service.fireworks.infrastructure.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * OpenAI Controller
 *
 * @author vyckey
 */
@Slf4j
@Controller
@RequestMapping("api/openai")
@AllArgsConstructor
public class OpenAIController {

    @PostMapping(value = "/v1/chat/completions")
    public Object chatCompletion(
            @RequestHeader(value = "Authorization") String authorization,
            @RequestParam(value = "baseUrl", required = false) String baseUrl,
            @RequestBody OpenAiApi.ChatCompletionRequest request) {
        String apiKey = authorization.replace("Bearer ", "");

        OpenAiApi.Builder builder = OpenAiApi.builder().apiKey(apiKey);
        if (StringUtils.isNotEmpty(baseUrl)) {
            builder.baseUrl(baseUrl);
        }

        OpenAiApi openAiApi = builder.build();
        if (BooleanUtils.isTrue(request.stream())) {
            return openAiApi.chatCompletionStream(request);
        } else {
            return openAiApi.chatCompletionEntity(request);
        }
    }

}
