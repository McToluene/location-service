package com.mctoluene.locationservice.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.mctoluene.locationservice.models.City;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CityRepository extends R2dbcRepository<City, UUID> {

    Mono<City> findByPublicId(UUID publicId);

    Mono<City> findCityByName(String cityName);

    Mono<City> findCityByNameAndStateProvinceId(String cityName, UUID stateProvinceId);

    Flux<City> findAllByStateProvinceId(UUID stateProvinceId, Pageable pageable);

    Mono<Long> countByStateProvinceId(UUID stateProvinceId);

    Mono<Long> countByStatusAndStateProvinceId(String status, UUID stateProvinceId);

    Flux<City> findAllByStatusAndStateProvinceId(String status, UUID stateProvinceId, Pageable pageable);

    Flux<City> findAllByNameContainingIgnoreCaseAndStateProvinceId(String name, UUID stateProvinceId,
            Pageable pageable);

    Flux<City> findAllByNameContainingIgnoreCaseAndStatusAndStateProvinceId(String name, String status,
            UUID stateProvinceId, Pageable pageable);

    Mono<Long> countByNameContainingIgnoreCaseAndStateProvinceId(String name, UUID stateProvinceId);

    Mono<Long> countByStatusAndNameContainingIgnoreCaseAndStateProvinceId(String status, String name,
            UUID stateProvinceId);

}
