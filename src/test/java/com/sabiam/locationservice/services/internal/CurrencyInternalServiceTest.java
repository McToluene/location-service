package com.sabiam.locationservice.services.internal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.models.Currency;
import com.mctoluene.locationservice.repositories.CurrencyRepository;
import com.mctoluene.locationservice.services.internal.CurrencyInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.impl.CurrencyInternalServiceImpl;
import com.mctoluene.locationservice.services.main.impl.CurrencyServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CurrencyInternalServiceTest {

    @Mock
    MessageSourceService messageSourceService;

    @Mock
    CurrencyRepository currencyRepository;

    Currency currency;

    CurrencyInternalService currencyInternalService;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        currencyInternalService = new CurrencyInternalServiceImpl(currencyRepository, messageSourceService);
        currency = buildCurrencyEntity();
    }

    @Test
    void saveCurrency() {
        when(currencyRepository.save(any())).thenReturn(Mono.just(currency));
        var saveCurrency = currencyInternalService.saveCurrency(currency);

        StepVerifier.create(saveCurrency)
                .consumeNextWith(currency -> {
                    assertThat(currency).isNotNull();
                    assertThat(currency.getName()).isNotBlank();
                })
                .verifyComplete();
    }

    @Test
    void getAllCurrencies() {
        Pageable pageable = PageRequest.of(0, 1);
        when(currencyRepository.findAllByStatus(pageable, Status.ACTIVE.name())).thenReturn(Flux.just(currency));
        var allCurrency = currencyInternalService.getAllCurrencies(pageable);
        StepVerifier.create(allCurrency)
                .expectNext(currency)
                .expectComplete()
                .verify();
    }

    Currency buildCurrencyEntity() {
        Currency currency = Currency
                .builder()
                .name("Algerian dinar")
                .code("DZD")
                .publicId(UUID.randomUUID())
                .build();
        currency.setCreatedBy("Kunal");
        currency.setCreatedDate(LocalDateTime.now());
        return currency;
    }

}