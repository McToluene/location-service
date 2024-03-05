package com.mctoluene.locationservice.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.mctoluene.locationservice.models.Currency;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public interface CurrencyRepository extends R2dbcRepository<Currency, UUID> {
    Mono<Currency> findByNameIgnoreCase(String name);

    Flux<Currency> findAllByStatus(Pageable pageable, String status);

    Mono<Long> countByStatus(String name);

    Flux<Currency> findAllByPublicIdIn(List<UUID> publicId);
}
