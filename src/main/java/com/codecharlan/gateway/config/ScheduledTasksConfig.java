package com.codecharlan.gateway.config;

import com.codecharlan.gateway.service.HttpBasedChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@EnableScheduling
public class ScheduledTasksConfig {
    private final HttpBasedChecker httpBasedChecker;

    @Scheduled(fixedRate = 300000)
    public void updateDeviceStatusTask() {
        httpBasedChecker.updateDeviceStatus();
    }
}
