package com.mctoluene.locationservice.controllers;

import com.mctoluene.locationservice.services.internal.TracingService;
import com.mctoluene.locationservice.services.main.ValidateCountryStateLgaService;
import com.mctolueneam.commons.response.AppResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/location/validate")
public class ValidationController {

    private final TracingService tracingService;

    private final ValidateCountryStateLgaService validationService;

    @GetMapping
    public Mono<ResponseEntity<AppResponse>> validateCountryStateLga(
            @RequestParam("countryPublicId") UUID countryPublicId,
            @RequestParam("statePublicId") UUID statePublicId,
            @RequestParam("localGovernmentPublicId") UUID localGovernmentPublicId,
            @RequestHeader("x-trace-id") String traceId) {
        log.info("request to validate countryPublicId: {}, statePublicId: {}, localGovernmentPublicId: {}",
                countryPublicId, statePublicId, localGovernmentPublicId);
        tracingService.propagateSleuthFields(traceId);
        return validationService.validateCountryStateLga(countryPublicId, statePublicId, localGovernmentPublicId)
                .map(ResponseEntity::ok);
    }

}
