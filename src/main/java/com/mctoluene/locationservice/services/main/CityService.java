package com.mctoluene.locationservice.services.main;

import com.mctoluene.locationservice.domains.dtos.CityDto;
import com.mctoluene.locationservice.domains.requestdtos.CityRequestDto;
import com.mctolueneam.commons.response.AppResponse;

import org.springframework.data.domain.PageImpl;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CityService {

    Mono<AppResponse<CityDto>> createNewCity(CityRequestDto cityRequestDto);

    Mono<AppResponse<CityDto>> findCityByPublicId(UUID publicId);

    Mono<AppResponse<CityDto>> findCityByNameStateCodeAndCountryCode(String name, String countryCode, String stateCode);

    Mono<AppResponse<CityDto>> deactivateCity(UUID publicId);

    Mono<AppResponse<CityDto>> activateCity(UUID publicId);

    Mono<AppResponse<PageImpl<CityDto>>> findAllCities(int page, int size, String status, UUID stateProvinceId,
            String name, String sort);
}
