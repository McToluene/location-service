package com.mctoluene.locationservice.services.main;

import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.domains.requestdtos.StateProvinceRequestDto;
import com.mctolueneam.commons.response.AppResponse;

import org.springframework.data.domain.PageImpl;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface StateProvinceService {

    Mono<AppResponse<StateProvinceDto>> createNewStateProvince(StateProvinceRequestDto stateProvinceRequestDto);

    Mono<AppResponse<StateProvinceDto>> findStateProvinceByPublicId(UUID publicId);

    Mono<AppResponse<StateProvinceDto>> findStateProvinceByCodeAndCountryCode(String countryCode, String code);

    Mono<AppResponse<StateProvinceDto>> deactivateStateProvince(UUID publicId);

    Mono<AppResponse<StateProvinceDto>> activateStateProvince(UUID publicId);

    Mono<AppResponse<PageImpl<StateProvinceDto>>> findAllStateProvinces(int page, int size, String status,
            UUID countryId, String name, String sort);

    Mono<AppResponse<List<StateProvinceDto>>> findStateProvinceByStatePublicIds(List<UUID> statePublicIds);
}
