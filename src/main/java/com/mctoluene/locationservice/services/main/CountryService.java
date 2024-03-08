package com.mctoluene.locationservice.services.main;

import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.domains.dtos.UpdateCountryDto;
import com.mctolueneam.commons.response.AppResponse;

import org.springframework.data.domain.PageImpl;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface CountryService {

    Mono<AppResponse<CountryDto>> findCountryByPublicId(UUID publicId);

    Mono<AppResponse<CountryDto>> findCountryByCode(String code);

    Mono<AppResponse<CountryDto>> findCountryByDialingCode(String dialingCode);

    Mono<AppResponse<CountryDto>> deactivateCountry(UUID publicId);

    Mono<AppResponse<CountryDto>> activateCountry(UUID publicId);

    Mono<AppResponse<PageImpl<CountryDto>>> findAllCountries(int page, int size, String status, String name,
            String sort);

    Mono<AppResponse<CountryDto>> update(UUID publicId, UpdateCountryDto body);

    Mono<AppResponse<List<CountryDto>>> findCountryByPublicIds(List<UUID> ids);
}
