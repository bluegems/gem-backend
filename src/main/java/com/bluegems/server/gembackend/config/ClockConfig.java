package com.bluegems.server.gembackend.config;

import org.springframework.context.annotation.Bean;

import java.time.Clock;

public class ClockConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}