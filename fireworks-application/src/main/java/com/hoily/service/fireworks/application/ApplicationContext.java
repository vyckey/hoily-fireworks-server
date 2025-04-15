package com.hoily.service.fireworks.application;

import com.hoily.service.fireworks.acl.AclContext;
import com.hoily.service.fireworks.infrastructure.InfrastructureContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@ComponentScan("com.hoily.service.fireworks.application")
@Import({InfrastructureContext.class, AclContext.class})
public class ApplicationContext {

}
