package com.mctoluene.locationservice.services.internal;

import org.springframework.data.domain.Pageable;

import com.mctoluene.locationservice.models.Currency;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface CurrencyInternalService {
    Mono<Currency> saveCurrency(Currency currency);

    Mono<Currency> findByName(String name);

    Flux<Currency> getAllCurrencies(Pageable pageable);

    Mono<Long> countByStatus();

    Flux<Currency> findByPublicId(List<UUID> publicId);
}
