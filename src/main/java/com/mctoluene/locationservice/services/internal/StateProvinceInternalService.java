package com.mctoluene.locationservice.services.internal;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.models.StateProvince;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface StateProvinceInternalService {
    Mono<StateProvince> saveStateProvince(StateProvince stateProvince);

    Mono<StateProvince> findStateProvinceByPublicId(UUID publicId);

    Mono<StateProvince> findStateProvinceByCode(String code);

    Mono<StateProvince> findStateProvinceByName(String stateName);

    Flux<StateProvince> findStateProvinceByCountry(Pageable pageable, Country country);

    Flux<StateProvince> findStateProvinceByCountryAndStatus(Country country, String status);

    Mono<Long> countByStatusAndCountryId(String status, UUID countryId);

    Mono<PageImpl<StateProvinceDto>> convertStateProvinceToPageableDto(Flux<StateProvince> stateProvinces,
            Pageable pageable, Mono<Long> stateProvinceCount);

    Mono<StateProvince> findById(UUID stateProvinceId);

    Flux<StateProvince> findAll(UUID countryId, Pageable pageable);

    Flux<StateProvince> findAllByName(UUID countryId, String stateName, Pageable pageable);

    Flux<StateProvince> findAllByStatus(UUID countryId, String status, Pageable pageable);

    Flux<StateProvince> findAllByStatusAndName(UUID countryId, String status, String stateName, Pageable pageable);

    Mono<Long> countByCountryId(UUID countryId);

    Mono<Long> countByNameAndCountryId(UUID countryId, String name);

    Flux<StateProvince> findStateProvinceByPublicIds(List<UUID> statePublicIds);
}
