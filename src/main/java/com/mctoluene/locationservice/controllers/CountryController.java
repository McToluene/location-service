package com.mctoluene.locationservice.controllers;

import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.domains.dtos.UpdateCountryDto;
import com.mctoluene.locationservice.domains.requestdtos.IdListDto;
import com.mctoluene.locationservice.services.internal.TracingService;
import com.mctoluene.locationservice.services.main.CountryService;
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
@RequestMapping("api/v1/location/countries")
public class CountryController {

    private final CountryService countryService;

    private final TracingService tracingService;

    private static final String RESPONSE_LOG = "final response {} ";

    @GetMapping("/{publicId}")
    public Mono<ResponseEntity<AppResponse<CountryDto>>> findCountryByPublicId(@PathVariable("publicId") UUID publicId,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return countryService.findCountryByPublicId(publicId)
                .doOnNext(appResponse -> log.info(RESPONSE_LOG, appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @PostMapping("/by-ids")
    public Mono<ResponseEntity<AppResponse<List<CountryDto>>>> findCountryByPublicIds(
            @RequestBody @Valid IdListDto data,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return countryService.findCountryByPublicIds(data.getIds())
                .doOnNext(appResponse -> log.info(RESPONSE_LOG, appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{publicId}/activate")
    public Mono<ResponseEntity<AppResponse<CountryDto>>> activateCountry(@PathVariable("publicId") UUID publicId,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return countryService.activateCountry(publicId).map(ResponseEntity::ok);
    }

    @PatchMapping("/{publicId}/deactivate")
    public Mono<ResponseEntity<AppResponse<CountryDto>>> deactivateCountry(@PathVariable("publicId") UUID publicId,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return countryService.deactivateCountry(publicId).map(ResponseEntity::ok);
    }

    @GetMapping
    public Mono<ResponseEntity<AppResponse<PageImpl<CountryDto>>>> findAllCountries(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @RequestParam(value = "status", defaultValue = "", required = false) String status,
            @RequestParam(value = "sort", defaultValue = "", required = false) String sort,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return countryService.findAllCountries(page, size, status, name, sort).map(ResponseEntity::ok);
    }

    @PutMapping("/{publicId}")
    public Mono<ResponseEntity<AppResponse<CountryDto>>> updateCountry(@PathVariable("publicId") UUID publicId,
            @RequestHeader("x-trace-id") String traceId,
            @RequestBody @Valid UpdateCountryDto body) {
        tracingService.propagateSleuthFields(traceId);
        return countryService.update(publicId, body)
                .doOnNext(appResponse -> log.info(RESPONSE_LOG, appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }

    @GetMapping("/code/{code}")
    public Mono<ResponseEntity<AppResponse<CountryDto>>> findCountryByCode(@PathVariable("code") String code,
            @RequestHeader("x-trace-id") String traceId) {
        tracingService.propagateSleuthFields(traceId);
        return countryService.findCountryByCode(code)
                .doOnNext(appResponse -> log.info(RESPONSE_LOG, appResponse.getData().toString()))
                .map(ResponseEntity::ok);
    }
}
