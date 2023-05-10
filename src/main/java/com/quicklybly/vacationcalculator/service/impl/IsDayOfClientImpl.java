package com.quicklybly.vacationcalculator.service.impl;

import com.quicklybly.vacationcalculator.enums.DayStatus;
import com.quicklybly.vacationcalculator.service.IsDayOffClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.quicklybly.vacationcalculator.exception.AppException.CODE.INTEGRATION_FINAL_EXCEPTION;
import static com.quicklybly.vacationcalculator.exception.AppException.CODE.INTEGRATION_RETRY_EXCEPTION;

@Slf4j
@RequiredArgsConstructor
@Component
public class IsDayOfClientImpl implements IsDayOffClient {
    private static final String DELIMITER = " ";

    private final WebClient client;

    @Override
    public Map<LocalDate, DayStatus> getPeriodData(LocalDate startDate, LocalDate endDate) {
        var apiResponse = getResponse(startDate, endDate);
        return parsePeriodResponse(apiResponse, startDate);
    }

    private String getResponse(LocalDate startDate, LocalDate endDate) {
        var formatter = getFormatter();
        final var NUMBER_OF_RETRIES = 3;
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/getdata")
                        .queryParam("date1", startDate.format(formatter))
                        .queryParam("date2", endDate.format(formatter))
                        .queryParam("delimeter", DELIMITER)
                        .build())
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(String.class);
                    }
                    var e = INTEGRATION_RETRY_EXCEPTION.get();
                    log.warn(e.getMessage(), e);
                    return Mono.error(e);
                })
                .retryWhen(Retry.fixedDelay(NUMBER_OF_RETRIES, Duration.ofSeconds(1))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            var e = INTEGRATION_FINAL_EXCEPTION.get();
                            log.error(e.getMessage(), e);
                            throw e;
                        }))
                .block();
    }

    private Map<LocalDate, DayStatus> parsePeriodResponse(String responseBody, LocalDate startDate) {
        var daysResponse = List.of(responseBody.split(DELIMITER));
        var currentDate = startDate;
        var workingDayResponse = "0";
        var resultMap = new HashMap<LocalDate, DayStatus>();

        for (var dayResponse : daysResponse) {
            var dayStatus = workingDayResponse.equals(dayResponse) ? DayStatus.WORKING : DayStatus.DAY_OFF;
            resultMap.put(currentDate, dayStatus);
            currentDate = currentDate.plusDays(1);
        }
        return resultMap;
    }

    private DateTimeFormatter getFormatter() {
        var pattern = "yyyyMMdd";
        return DateTimeFormatter.ofPattern(pattern);
    }
}

