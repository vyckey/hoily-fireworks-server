package com.hoily.service.fireworks.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
@ComponentScan("com.hoily.service.fireworks.infrastructure")
public class InfrastructureContext {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
