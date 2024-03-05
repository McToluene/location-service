package com.mctoluene.locationservice.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.mctoluene.locationservice.models.LocalGovernment;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface LocalGovernmentRepository extends R2dbcRepository<LocalGovernment, UUID> {

    Mono<LocalGovernment> findByPublicId(UUID publicId);

    Mono<LocalGovernment> findByPublicIdAndStatus(UUID publicId, String status);

    Mono<LocalGovernment> findByNameAndCityId(String name, UUID cityId);

    Mono<LocalGovernment> findLocalGovernmentByName(String localGovernmentName);

    Flux<LocalGovernment> findAllByCityId(UUID cityId, Pageable pageable);

    Mono<Long> countByStatusAndCityId(String status, UUID cityId);

    Mono<Long> countByCityId(UUID cityId);

    Flux<LocalGovernment> findAllByStatusAndCityId(String status, UUID cityId, Pageable pageable);

    Flux<LocalGovernment> findAllByNameContainingIgnoreCaseAndCityId(String name, UUID cityId, Pageable pageable);

    Flux<LocalGovernment> findAllByNameContainingIgnoreCaseAndStatusAndCityId(String name, String status, UUID cityId,
            Pageable pageable);

    Mono<Long> countByNameContainingIgnoreCaseAndCityId(String name, UUID cityId);

    Mono<Long> countByStatusAndNameContainingIgnoreCaseAndCityId(String status, String name, UUID cityId);

    Flux<LocalGovernment> findByPublicIdIn(List<UUID> publicIds);
}
