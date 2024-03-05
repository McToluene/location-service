package com.sabiam.locationservice.services.main;

import com.mctoluene.locationservice.domains.dtos.CurrencyDto;
import com.mctoluene.locationservice.domains.requestdtos.CurrencyRequestDto;
import com.mctoluene.locationservice.models.Currency;
import com.mctoluene.locationservice.services.internal.CurrencyInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.main.CurrencyService;
import com.mctoluene.locationservice.services.main.impl.CurrencyServiceImpl;
import com.sabiam.commons.response.AppResponse;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CurrencyServiceTest {

    @Mock
    MessageSourceService messageSourceService;

    @Mock
    CurrencyInternalService currencyInternalService;

    AutoCloseable autoCloseable;

    CurrencyService currencyService;

    Currency currency;

    CurrencyDto currencyDto;

    Currency saveCurrency;

    CurrencyRequestDto currencyRequestDto;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        currencyService = new CurrencyServiceImpl(currencyInternalService, messageSourceService);
        currency = buildCurrencyEntity();
        currencyDto = buildCurrencyDto(currency);
        saveCurrency = SaveCurrencyEntity(currency);
        currencyRequestDto = buildCurrencyRequestDto();
    }

    @Test
    void createCurrency() {
        when(currencyInternalService.findByName(any())).thenReturn(Mono.empty());
        when(currencyInternalService.saveCurrency(any())).thenReturn(Mono.just(saveCurrency));
        Mono<AppResponse> result = currencyService.createCurrency(currencyRequestDto);
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
                })
                .verifyComplete();
    }

    @Test
    void findAllCurrency() {
        List<Currency> currencies = new ArrayList<>();
        currencies.add(currency);

        Pageable pageable = PageRequest.of(0, 10);
        when(currencyInternalService.getAllCurrencies(pageable)).thenReturn(Flux.just(currency));
        when(currencyInternalService.countByStatus()).thenReturn(Mono.just(1L));

        var result = currencyService.findAllCurrency(1, 10);

        StepVerifier
                .create(result)
                .consumeNextWith(appResponse -> {
                    assertThat(appResponse.getStatus()).isEqualTo(200);
                    assertThat(appResponse.getData()).isNotNull();
                    assertThat(appResponse.getData()).isNotNull();
                })
                .verifyComplete();
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

    Currency SaveCurrencyEntity(Currency currency) {
        currency.setId(UUID.randomUUID());
        currency.setCreatedDate(LocalDateTime.now());
        currency.setCreatedBy("Kunal");
        currency.setCode("DZD");
        currency.setName("Algerian dinar");
        return currency;
    }
}