package com.hoily.service.fireworks.acl.models;

import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description is here
 *
 * @author vyckey
 */
@Configuration
public class ModelApiConfiguration {
    @Bean
    public OpenAiApi openAiApi(@Value("${modelProviders.openai.baseUrl}") String baseUrl,
                               @Value("${modelProviders.openai.apiKey}") String apiKey) {
        return OpenAiApi.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .build();
    }
}
