package com.mctoluene.locationservice.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.mctoluene.locationservice.models.Country;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public interface CountryRepository extends R2dbcRepository<Country, UUID> {

    Mono<Country> findByPublicId(UUID publicId);

    Mono<Country> findByPublicIdAndStatus(UUID publicId, String status);

    Mono<Country> findCountryByDialingCode(String dialingCode);

    Mono<Country> findCountryByTwoLetterCode(String twoLetterCode);

    Mono<Country> findCountryByThreeLetterCode(String threeLetterCode);

    Flux<Country> findAllByStatus(String status, Pageable pageable);

    Flux<Country> findAllByStatusAndCountryNameContainingIgnoreCase(String status, String countryName,
            Pageable pageable);

    Flux<Country> findAllByCountryNameContainingIgnoreCase(String countryName, Pageable pageable);

    Flux<Country> findAllBy(Pageable pageable);

    Mono<Country> findCountryByCountryName(String countryName);

    Mono<Long> countByStatus(String status);

    Mono<Long> countByCountryNameContainingIgnoreCase(String countryName);

    Mono<Long> countByStatusAndCountryNameContainingIgnoreCase(String status, String countryName);

    Flux<Country> findByPublicIdIn(List<UUID> publicIds);
}
