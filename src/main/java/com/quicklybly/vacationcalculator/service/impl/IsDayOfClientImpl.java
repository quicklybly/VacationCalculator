package com.quicklybly.vacationcalculator.service.impl;

import com.quicklybly.vacationcalculator.enums.DayStatus;
import com.quicklybly.vacationcalculator.exception.IntegrationException;
import com.quicklybly.vacationcalculator.service.IsDayOffClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //TODO resolve dns exception, retry delay
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
                    return Mono.error(IntegrationException::new);
                })
                .retryWhen(Retry.max(NUMBER_OF_RETRIES)
                        .filter(throwable -> throwable instanceof IntegrationException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new IntegrationException();
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

