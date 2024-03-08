package com.mctolueneam.locationservice.controllers;

import com.mctoluene.locationservice.controllers.CurrencyController;
import com.mctoluene.locationservice.domains.dtos.CurrencyDto;
import com.mctoluene.locationservice.domains.requestdtos.CurrencyRequestDto;
import com.mctoluene.locationservice.models.Currency;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.TracingService;
import com.mctoluene.locationservice.services.main.CurrencyService;
import com.mctolueneam.commons.response.AppResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CurrencyService currencyService;

    @MockBean
    TracingService tracingService;

    @MockBean
    MessageSourceService messageSourceService;

    Currency currency;
    CurrencyDto currencyDto;

    @BeforeEach
    void setUp() {

        currency = buildCurrencyEntity();
        currencyDto = buildCurrencyDto(currency);
    }

    @Test
    void createCurrency() {

        Mono<AppResponse> response = Mono.just(new AppResponse(HttpStatus.CREATED.value(),
                "currency created successfully",
                "currency created successfully", currencyDto, null));
        var requestDto = buildCurrencyRequestDto();
        when(currencyService.createCurrency(requestDto)).thenReturn(response);
        webTestClient.post()
                .uri("/api/v1/location/currency").contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .header("x-trace-id", UUID.randomUUID().toString())
                .body(Mono.just(requestDto), CurrencyRequestDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .json("{\"status\":201,\"message\":\"currency created successfully\",\"supportDescriptiveMessage\":\"currency created successfully\"}")
                .jsonPath("$.status").isEqualTo(201)
                .jsonPath("$.message").isEqualTo("currency created successfully")
                .jsonPath("$.supportDescriptiveMessage").isEqualTo("currency created successfully");
    }

    @Test
    void findAllCurrency() {
        Mono<AppResponse> responseEntityMono = Mono.just(new AppResponse(HttpStatus.OK.value(),
                "currency.fetched.successfully", "currency.fetched.successfully", new ArrayList<>(), null));
        Integer page = 1;
        Integer size = 10;
        when(currencyService.findAllCurrency(page, size)).thenReturn(responseEntityMono);
        webTestClient
                .get()
                .uri("/api/v1/location/currency?page=" + page + "&size=" + size)
                .header("x-trace-id", UUID.randomUUID().toString())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json("{\"status\":200,\"message\":\"currency.fetched.successfully\",\"supportDescriptiveMessage\":\"currency.fetched.successfully\"}");
    }

    Currency buildCurrencyEntity() {
        Currency currency = Currency
                .builder()
                .name("Algerian dinar")
                .publicId(UUID.randomUUID())
                .build();

        currency.setCode("DZD");
        currency.setCreatedBy("Kunal");
        currency.setCreatedDate(LocalDateTime.now());
        return currency;
    }

    CurrencyDto buildCurrencyDto(Currency currency) {
        return new CurrencyDto(currency.getPublicId(), currency.getCode(), currency.getName(),
                currency.getCreatedDate(), currency.getLastModifiedDate(), currency.getCreatedBy(),
                currency.getLastModifiedBy(), currency.getStatus());
    }

    CurrencyRequestDto buildCurrencyRequestDto() {
        return CurrencyRequestDto.builder()
                .code("DZD")
                .name("Algerian dinar")
                .createdBy("Kunal")
                .build();
    }

}