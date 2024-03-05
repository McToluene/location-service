package com.mctoluene.locationservice.controllers;

import com.mctoluene.locationservice.domains.requestdtos.CurrencyRequestDto;
import com.mctoluene.locationservice.helpers.CurrencyHelper;
import com.mctoluene.locationservice.services.internal.TracingService;
import com.mctoluene.locationservice.services.main.CurrencyService;
import com.sabiam.commons.response.AppResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/location/currency")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final TracingService tracingService;

    @PostMapping
    public Mono<ResponseEntity<AppResponse>> createCurrency(@RequestHeader("x-trace-id") String traceId,
            @RequestBody @Valid CurrencyRequestDto currencyRequestDto) {
        tracingService.propagateSleuthFields(traceId);
        return currencyService.createCurrency(currencyRequestDto)
                .doOnNext(appResponse -> log.info("final response create new currency{} ",
                        appResponse.getData().toString()))
                .map(CurrencyHelper::buildCurrencyResponse);
    }

    @GetMapping
    public Mono<ResponseEntity<AppResponse>> findAllCurrency(@RequestHeader("x-trace-id") String traceId,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        tracingService.propagateSleuthFields(traceId);
        return currencyService.findAllCurrency(page, size)
                .doOnNext(appResponse -> log.info("final response {} ", appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

}
