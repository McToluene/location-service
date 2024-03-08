package com.mctoluene.locationservice.services.main.impl;

import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.models.StateProvince;
import com.mctoluene.locationservice.services.internal.CountryInternalService;
import com.mctoluene.locationservice.services.internal.LocalGovernmentInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.StateProvinceInternalService;
import com.mctoluene.locationservice.services.main.ValidateCountryStateLgaService;
import com.mctolueneam.commons.exceptions.NotFoundException;
import com.mctolueneam.commons.response.AppResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidateCountryStateLgaServiceImpl implements ValidateCountryStateLgaService {

    private final CountryInternalService countryInternalService;
    private final StateProvinceInternalService stateProvinceInternalService;
    private final LocalGovernmentInternalService localGovernmentInternalService;

    private final MessageSourceService messageSourceService;

    @Override
    public Mono<AppResponse<List<String>>> validateCountryStateLga(UUID countryPublicId, UUID statePublicId,
            UUID lgaPublicId) {

        List<String> response = new ArrayList<>();

        return countryInternalService.findCountryByPublicIdAndStatus(countryPublicId, Status.ACTIVE.toString())
                .map(country -> {
                    response.add("Country province public id: " + countryPublicId);
                    return stateProvinceInternalService.findStateProvinceByCountryAndStatus(country,
                            Status.ACTIVE.name());
                })
                .map(stateProvinceFlux -> stateProvinceFlux
                        .filter(stateProvince -> stateProvince.getPublicId().equals(statePublicId))
                        .switchIfEmpty(Mono.error(new NotFoundException(
                                messageSourceService.getMessageByKey("state.not.found.in.country")))))
                .flatMapMany(r -> r.map(StateProvince::getPublicId))
                .doOnNext(publicId -> response.add("State province public id: " + publicId.toString()))
                .zipWith(localGovernmentInternalService
                        .findLocalGovernmentByPublicIdAndStatus(lgaPublicId, Status.ACTIVE.name())
                        .map(localGovernment -> {
                            response.add("Local Government public id: " + localGovernment.getPublicId().toString());
                            return localGovernment.getPublicId();
                        }))
                .collectList()
                .map(r -> new AppResponse<>(HttpStatus.OK.value(),
                        messageSourceService.getMessageByKey("country.state.lga.validated.successfully"),
                        messageSourceService.getMessageByKey("country.state.lga.validated.successfully"),
                        response, null));
    }
}
