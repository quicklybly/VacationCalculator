package com.quicklybly.vacationcalculator.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@RequiredArgsConstructor
@Configuration
public class AppConfig {
    private static final Integer TIMEOUT = 5000;

    @Bean
    public WebClient webClient() {
        var httpClient = getHttpClient();
        return WebClient.builder()
                .baseUrl("https://isdayoff.ru/")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    private HttpClient getHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, AppConfig.TIMEOUT)
                .responseTimeout(Duration.ofMillis(AppConfig.TIMEOUT))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(AppConfig.TIMEOUT, MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(AppConfig.TIMEOUT, MILLISECONDS)));
    }
}
