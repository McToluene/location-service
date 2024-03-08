package com.mctoluene.locationservice.services.main;

import com.mctolueneam.commons.response.AppResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ValidateCountryStateLgaService {

    Mono<AppResponse<List<String>>> validateCountryStateLga(UUID countryPublicId, UUID statePublicId, UUID lgaPublicId);
}
