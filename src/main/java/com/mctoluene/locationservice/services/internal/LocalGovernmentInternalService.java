package com.mctoluene.locationservice.services.internal;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mctoluene.locationservice.domains.dtos.LocalGovernmentDto;
import com.mctoluene.locationservice.models.City;
import com.mctoluene.locationservice.models.LocalGovernment;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface LocalGovernmentInternalService {

    Mono<LocalGovernment> saveLocalGovernment(LocalGovernment localGovernment);

    Mono<LocalGovernment> findLocalGovernmentByPublicId(UUID publicId);

    Flux<LocalGovernment> findLocalGovernmentByPublicIds(List<UUID> publicIds);

    Mono<LocalGovernment> findLocalGovernmentByPublicIdAndStatus(UUID publicId, String status);

    Mono<LocalGovernment> findLocalGovernmentByName(String localGovernmentName);

    Mono<LocalGovernment> findLocalGovernmentByNameAndCity(String localGovernmentName, City city);

    Flux<LocalGovernment> findLocalGovernmentByCity(Pageable pageable, City city);

    Mono<PageImpl<LocalGovernmentDto>> convertLocalGovernmentToPageableDto(Flux<LocalGovernment> localGovernmentFlux,
            Pageable pageable, City city, Mono<Long> localGovernmentCount);

    Mono<Long> countByStatusAndCityId(String status, UUID cityId);

    Flux<LocalGovernment> findAll(UUID cityId, Pageable pageable);

    Mono<Long> countByCityId(UUID cityId);

    Flux<LocalGovernment> findAllByStatus(UUID cityId, String status, Pageable pageable);

    Flux<LocalGovernment> findAllByName(UUID cityId, String name, Pageable pageable);

    Mono<Long> countByNameAndCityId(UUID cityId, String name);

    Flux<LocalGovernment> findAllByStatusAndName(UUID cityId, String status, String name, Pageable pageable);

    Mono<Long> countByNameAndStatusAndCityId(UUID cityId, String status, String name);
}
