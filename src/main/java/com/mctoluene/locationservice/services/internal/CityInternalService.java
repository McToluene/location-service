package com.mctoluene.locationservice.services.internal;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mctoluene.locationservice.domains.dtos.CityDto;
import com.mctoluene.locationservice.models.City;
import com.mctoluene.locationservice.models.StateProvince;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CityInternalService {
    Mono<City> saveCity(City city);

    Mono<City> findCityByPublicId(UUID publicId);

    Mono<City> findCityByNameAndState(String name, StateProvince stateProvince);

    Mono<City> findCityByName(String cityName);

    Flux<City> findCityByStateProvince(Pageable pageable, StateProvince stateProvince);

    Mono<PageImpl<CityDto>> convertCityToPageableDto(Flux<City> cities, Pageable pageable, Mono<Long> cityCount);

    Mono<City> findById(UUID cityId);

    Mono<Long> countByStatusAndStateProvinceId(String status, UUID stateProvinceId);

    Flux<City> findAll(UUID stateProvinceId, Pageable pageable);

    Flux<City> findAllByStatus(UUID stateProvinceId, String status, Pageable pageable);

    Flux<City> findAllByName(UUID stateProvinceId, String name, Pageable pageable);

    Mono<Long> countByStateProvinceId(UUID stateProvinceId);

    Flux<City> findAllByStatusAndName(UUID stateProvinceId, String status, String name, Pageable pageable);

    Mono<Long> countByNameAndStateProvinceId(UUID stateProvinceId, String name);

    Mono<Long> countByNameAndStatusAndStateProvinceId(UUID stateProvinceId, String status, String name);
}
