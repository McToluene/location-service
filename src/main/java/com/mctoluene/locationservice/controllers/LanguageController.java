package com.mctoluene.locationservice.controllers;

import com.mctoluene.locationservice.domains.requestdtos.LanguageRequestDto;
import com.mctoluene.locationservice.helpers.LanguageHelper;
import com.mctoluene.locationservice.services.internal.TracingService;
import com.mctoluene.locationservice.services.main.LanguageService;
import com.sabiam.commons.response.AppResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/location/languages")
public class LanguageController {

    private final LanguageService languageService;

    private final TracingService tracingService;

    private static final String RESPONSE_LOG = "final response {} ";

    @PostMapping
    public Mono<ResponseEntity<AppResponse>> createNewLanguage(
            @RequestBody @Valid LanguageRequestDto languageRequestDto,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return languageService.createNewLanguage(languageRequestDto)
                .doOnNext(appResponse -> log.info(RESPONSE_LOG, appResponse.getData().toString()))
                .map(LanguageHelper::buildLanguageResponse);
    }

    @GetMapping("/{publicId}")
    public Mono<ResponseEntity<AppResponse>> findLanguageByPublicId(@PathVariable("publicId") UUID publicId,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return languageService.findLanguageByPublicId(publicId)
                .doOnNext(appResponse -> log.info(RESPONSE_LOG, appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Mono<ResponseEntity<AppResponse>> findAllLanguages(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return languageService.findAllLanguages(page, size)
                .doOnNext(appResponse -> log.info(RESPONSE_LOG, appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }
}
