package com.mctoluene.locationservice.controllers;

import com.mctoluene.locationservice.domains.dtos.CityDto;
import com.mctoluene.locationservice.domains.requestdtos.CityRequestDto;
import com.mctoluene.locationservice.helpers.CityHelper;
import com.mctoluene.locationservice.services.internal.TracingService;
import com.mctoluene.locationservice.services.main.CityService;
import com.mctolueneam.commons.response.AppResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/location/cities")
public class CityController {

    private final CityService cityService;

    private final TracingService tracingService;

    private static final String RESPONSE_LOG = "final response {} ";

    @PostMapping
    public Mono<ResponseEntity<AppResponse<CityDto>>> createNewCity(@RequestBody @Valid CityRequestDto cityRequestDto,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return cityService.createNewCity(cityRequestDto)
                .doOnNext(appResponse -> log.info(RESPONSE_LOG, appResponse.getData().toString()))
                .map(CityHelper::buildCityResponse);
    }

    @GetMapping("/{publicId}")
    public Mono<ResponseEntity<AppResponse<CityDto>>> findCityByPublicId(@PathVariable("publicId") UUID publicId,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return cityService.findCityByPublicId(publicId)
                .doOnNext(appResponse -> log.info(RESPONSE_LOG, appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{publicId}/deactivate")
    public Mono<ResponseEntity<AppResponse<CityDto>>> deactivateCity(@PathVariable("publicId") UUID publicId,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return cityService.deactivateCity(publicId)
                .doOnNext(appResponse -> log.info(RESPONSE_LOG, appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{publicId}/activate")
    public Mono<ResponseEntity<AppResponse<CityDto>>> activateCity(@PathVariable("publicId") UUID publicId,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return cityService.activateCity(publicId)
                .doOnNext(appResponse -> log.info(RESPONSE_LOG, appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Mono<ResponseEntity<AppResponse<PageImpl<CityDto>>>> findAllCities(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam("stateProvinceId") UUID stateProvinceId,
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @RequestParam(value = "status", defaultValue = "", required = false) String status,
            @RequestParam(value = "sort", defaultValue = "", required = false) String sort,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return cityService.findAllCities(page, size, status, stateProvinceId, name, sort)
                .doOnNext(appResponse -> log.info(RESPONSE_LOG, appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{country-code}/{stateCode}/{cityName}")
    public Mono<ResponseEntity<AppResponse<CityDto>>> findCityByNameStateCodeAndCountryCode(
            @PathVariable("country-code") String countryCode,
            @PathVariable("stateCode") String stateCode,
            @PathVariable("cityName") String cityName,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return cityService.findCityByNameStateCodeAndCountryCode(cityName, countryCode, stateCode)
                .doOnNext(appResponse -> log.info(RESPONSE_LOG, appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }
}
