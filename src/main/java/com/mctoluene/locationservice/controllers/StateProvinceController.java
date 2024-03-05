package com.mctoluene.locationservice.controllers;

import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.domains.requestdtos.StateProvinceRequestDto;
import com.mctoluene.locationservice.helpers.StateProvinceHelper;
import com.mctoluene.locationservice.services.internal.TracingService;
import com.mctoluene.locationservice.services.main.StateProvinceService;
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
@RequestMapping("api/v1/location/state-provinces")
public class StateProvinceController {

    private final StateProvinceService stateProvinceService;

    private final TracingService tracingService;

    @PostMapping
    public Mono<ResponseEntity<AppResponse<StateProvinceDto>>> createNewStateProvince(
            @RequestBody @Valid StateProvinceRequestDto stateProvinceRequestDto,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return stateProvinceService.createNewStateProvince(stateProvinceRequestDto)
                .doOnNext(appResponse -> log.info("final response create new state {} ",
                        appResponse.getData().toString()))
                .map(StateProvinceHelper::buildStateProvinceResponse);
    }

    @GetMapping("/{publicId}")
    public Mono<ResponseEntity<AppResponse<StateProvinceDto>>> findStateProvinceByPublicId(
            @PathVariable("publicId") UUID publicId,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return stateProvinceService.findStateProvinceByPublicId(publicId)
                .doOnNext(appResponse -> log.info("final response for find state by public id{} ",
                        appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{publicId}/deactivate")
    public Mono<ResponseEntity<AppResponse<StateProvinceDto>>> deactivateStateProvince(
            @PathVariable("publicId") UUID publicId,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return stateProvinceService.deactivateStateProvince(publicId)
                .doOnNext(appResponse -> log.info("final response deactivate state {} ",
                        appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{publicId}/activate")
    public Mono<ResponseEntity<AppResponse<StateProvinceDto>>> activateStateProvince(
            @PathVariable("publicId") UUID publicId,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return stateProvinceService.activateStateProvince(publicId)
                .doOnNext(appResponse -> log.info("final response {} ",
                        appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Mono<ResponseEntity<AppResponse<PageImpl<StateProvinceDto>>>> findAllStateProvinces(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam("countryId") UUID countryId,
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @RequestParam(value = "status", defaultValue = "", required = false) String status,
            @RequestParam(value = "sort", defaultValue = "", required = false) String sort,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return stateProvinceService.findAllStateProvinces(page, size, status, countryId, name, sort)
                .doOnNext(appResponse -> log.info("final response {} ",
                        appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @PostMapping("/states")
    public Mono<ResponseEntity<AppResponse<List<StateProvinceDto>>>> findStateProvinceByPublicIds(
            @RequestBody List<UUID> statePublicIds,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return stateProvinceService.findStateProvinceByStatePublicIds(statePublicIds)
                .doOnNext(appResponse -> log.info("final response for find state by public ids{} ",
                        appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{country-code}/state-code/{code}")
    public Mono<ResponseEntity<AppResponse<StateProvinceDto>>> findStateProvinceByCodeAndCountryCode(
            @PathVariable("country-code") String countryCode,
            @PathVariable("code") String code,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return stateProvinceService.findStateProvinceByCodeAndCountryCode(countryCode, code)
                .doOnNext(appResponse -> log.info("final response for find state by code{} ",
                        appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }
}
