package com.mctoluene.locationservice.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.mctoluene.locationservice.models.StateProvince;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public interface StateProvinceRepository extends R2dbcRepository<StateProvince, UUID> {

    Mono<StateProvince> findByPublicId(UUID publicId);

    Mono<StateProvince> findByCode(String code);

    Mono<StateProvince> findStateProvinceByName(String stateName);

    Flux<StateProvince> findAllBy(UUID countryId, Pageable pageable);

    Flux<StateProvince> findAllByCountryId(Pageable pageable, UUID countryId);

    Flux<StateProvince> findAllByCountryIdAndStatus(UUID countryId, String status);

    Mono<Long> countByCountryId(UUID countryId);

    Mono<Long> countByStatusAndCountryId(String status, UUID countryId);

    Flux<StateProvince> findAllByStatusAndCountryId(String status, UUID countryId, Pageable pageable);

    Flux<StateProvince> findAllByNameContainingIgnoreCaseAndCountryId(String name, UUID countryId, Pageable pageable);

    Flux<StateProvince> findAllByNameContainingIgnoreCaseAndStatusAndCountryId(String name, String status,
            UUID countryId, Pageable pageable);

    Mono<Long> countByNameContainingIgnoreCaseAndCountryId(String name, UUID countryId);

    Flux<StateProvince> findAllByPublicIdIn(List<UUID> statePublicIds);
}
