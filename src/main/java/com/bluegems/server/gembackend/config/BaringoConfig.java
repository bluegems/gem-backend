package com.bluegems.server.gembackend.config;

import com.github.kskelm.baringo.BaringoClient;
import com.github.kskelm.baringo.util.BaringoApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.http.HttpClient;

@Slf4j
@Configuration
public class BaringoConfig {

    @Value("${imgur.client-id}")
    private String IMGUR_CLIENT_ID;

    @Value("${imgur.client-secret}")
    private String IMGUR_CLIENT_SECRET;

    @Value("${imgur.refresh-token}")
    private String IMGUR_REFRESH_TOKEN;

    @Bean
    public BaringoClient baringoClient() throws BaringoApiException {
        log.info("REFRESH TOKEN : {}", IMGUR_REFRESH_TOKEN);
        BaringoClient baringoClient = new BaringoClient.Builder()
                .clientAuth(IMGUR_CLIENT_ID, IMGUR_CLIENT_SECRET)
                .build();
        baringoClient.authService().setRefreshToken(IMGUR_REFRESH_TOKEN);
        return baringoClient;
    }

}
