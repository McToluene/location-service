package com.mctoluene.locationservice.services.main;

import com.mctoluene.locationservice.domains.dtos.LocalGovernmentDto;
import com.mctoluene.locationservice.domains.requestdtos.LocalGovernmentRequestDto;
import com.sabiam.commons.response.AppResponse;

import org.springframework.data.domain.PageImpl;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface LocalGovernmentService {

    Mono<AppResponse<LocalGovernmentDto>> createNewLocalGovernment(LocalGovernmentRequestDto localGovernmentRequestDto);

    Mono<AppResponse<LocalGovernmentDto>> findLocalGovernmentByPublicId(UUID publicId);

    Mono<AppResponse<LocalGovernmentDto>> findLocalGovernmentByNameCityNameAndStateStatusCode(String name,
            String cityName, String stateCode);

    Mono<AppResponse<List<LocalGovernmentDto>>> findLocalGovernmentByPublicIds(List<UUID> publicIds);

    Mono<AppResponse<LocalGovernmentDto>> deactivateLocalGovernment(UUID publicId);

    Mono<AppResponse<LocalGovernmentDto>> activateLocalGovernment(UUID publicId);

    Mono<AppResponse<PageImpl<LocalGovernmentDto>>> findAllLocalGovernment(Integer page, Integer size, String status,
            UUID cityId, String name,
            String sort);
}
