package com.hoily.service.fireworks.server.controller;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health/")
public class HealthController implements ApplicationListener<ApplicationReadyEvent> {
    private volatile boolean ready = false;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        ready = true;
    }

    @RequestMapping(value = "/ready", method = {RequestMethod.HEAD, RequestMethod.GET})
    public String ready() {
        if (ready) {
            return "true";
        } else {
            throw new RuntimeException("service is unhealthy");
        }
    }
}