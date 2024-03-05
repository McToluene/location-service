package com.mctoluene.locationservice.controllers;

import com.mctoluene.locationservice.domains.dtos.LocalGovernmentDto;
import com.mctoluene.locationservice.domains.requestdtos.LocalGovernmentRequestDto;
import com.mctoluene.locationservice.helpers.LocalGovernmentHelper;
import com.mctoluene.locationservice.services.internal.TracingService;
import com.mctoluene.locationservice.services.main.LocalGovernmentService;
import com.sabiam.commons.response.AppResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/location/local-government")
public class LocalGovernmentController {

    private final LocalGovernmentService localGovernmentService;

    private final TracingService tracingService;

    @PostMapping
    public Mono<ResponseEntity<AppResponse<LocalGovernmentDto>>> createNewLocalGovernment(
            @RequestBody @Valid LocalGovernmentRequestDto localGovernmentRequestDto,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return localGovernmentService.createNewLocalGovernment(localGovernmentRequestDto)
                .doOnNext(appResponse -> log.info("final response create new local government {} ",
                        appResponse.getData().toString()))
                .map(LocalGovernmentHelper::buildLocalGovernmentResponse);
    }

    @GetMapping("/{publicId}")
    public Mono<ResponseEntity<AppResponse<LocalGovernmentDto>>> findLocalGovernmentByPublicId(
            @PathVariable("publicId") UUID publicId,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return localGovernmentService.findLocalGovernmentByPublicId(publicId)
                .doOnNext(appResponse -> log.info("final response for find local government by public id{} ",
                        appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @PostMapping("/details")
    public Mono<ResponseEntity<AppResponse<List<LocalGovernmentDto>>>> findLocalGovernmentByPublicIds(
            @RequestHeader("x-trace-id") String traceId,
            @RequestBody List<UUID> publicIds) {
        tracingService.propagateSleuthFields(traceId);
        return localGovernmentService.findLocalGovernmentByPublicIds(
                publicIds)
                .doOnNext(appResponse -> log.info("final response for find local government by public id{} ",
                        appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{publicId}/deactivate")
    public Mono<ResponseEntity<AppResponse<LocalGovernmentDto>>> deactivateLocalGovernment(
            @PathVariable("publicId") UUID publicId,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return localGovernmentService.deactivateLocalGovernment(publicId)
                .doOnNext(appResponse -> log.info("final response deactivate local government {} ",
                        appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{publicId}/activate")
    public Mono<ResponseEntity<AppResponse<LocalGovernmentDto>>> activateLocalGovernment(
            @PathVariable("publicId") UUID publicId,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return localGovernmentService.activateLocalGovernment(publicId)
                .doOnNext(appResponse -> log.info("final response {} ",
                        appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Mono<ResponseEntity<AppResponse<PageImpl<LocalGovernmentDto>>>> findAllLocalGovernments(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam("cityId") UUID cityId,
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @RequestParam(value = "status", defaultValue = "", required = false) String status,
            @RequestParam(value = "sort", defaultValue = "", required = false) String sort,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return localGovernmentService.findAllLocalGovernment(page, size, status, cityId, name, sort)
                .doOnNext(appResponse -> log.info("final response {} ",
                        appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{stateCode}/{cityName}/{lgaName}")
    public Mono<ResponseEntity<AppResponse<LocalGovernmentDto>>> findLocalGovernmentByNameCityNameAndStateStatusCode(
            @PathVariable("stateCode") String stateCode,
            @PathVariable("cityName") String cityName,
            @PathVariable("lgaName") String lgaName,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return localGovernmentService.findLocalGovernmentByNameCityNameAndStateStatusCode(lgaName, cityName, stateCode)
                .doOnNext(appResponse -> log.info("final response for find local government by code{} ",
                        appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }
}
