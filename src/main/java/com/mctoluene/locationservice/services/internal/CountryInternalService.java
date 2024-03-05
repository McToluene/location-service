package com.mctoluene.locationservice.services.internal;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.models.Country;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface CountryInternalService {
    Mono<Country> saveCountry(Country country);

    Mono<Country> findCountryByPublicId(UUID publicId);

    Mono<Country> findCountryByPublicIdAndStatus(UUID publicId, String status);

    Mono<Country> findCountryByDialingCode(String dialingCode);

    Mono<Country> findCountryByTwoLetterCode(String twoLetterCode);

    Mono<Country> findCountryByThreeLetterCode(String threeLetterCode);

    Mono<Country> findCountryByName(String countryName);

    Flux<Country> findAllByName(String countryName, Pageable pageable);

    Flux<Country> findAllByStatus(String status, Pageable pageable);

    Flux<Country> findAllByStatusAndName(String status, String countryName, Pageable pageable);

    Flux<Country> findAllCountry(Pageable pageable);

    Mono<PageImpl<CountryDto>> convertCountryToPageable(Flux<Country> countries, Pageable pageable,
            Mono<Long> countryCount);

    Mono<Country> findById(UUID countryId);

    Mono<Long> countByStatusAndName(String status, String name);

    Mono<Long> count();

    Mono<Long> countByStatus(String status);

    Mono<Long> countByName(String name);

    Flux<Country> findCountryByPublicIds(List<UUID> ids);
}
