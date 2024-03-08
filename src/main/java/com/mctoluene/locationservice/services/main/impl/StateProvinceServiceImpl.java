package com.mctoluene.locationservice.services.main.impl;

import com.mctoluene.locationservice.constants.MessageLog;
import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.StateProvinceRequestDto;
import com.mctoluene.locationservice.helpers.LocationHelper;
import com.mctoluene.locationservice.helpers.StateProvinceHelper;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.models.StateProvince;
import com.mctoluene.locationservice.services.internal.CountryInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.StateProvinceInternalService;
import com.mctoluene.locationservice.services.main.StateProvinceService;
import com.mctolueneam.commons.exceptions.ConflictException;
import com.mctolueneam.commons.response.AppResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StateProvinceServiceImpl implements StateProvinceService {

    private final CountryInternalService countryInternalService;

    private final StateProvinceInternalService stateProvinceInternalService;

    private final MessageSourceService messageSourceService;

    private static final String STATE_RETRIEVED = "state.province.retrieved.successfully";

    @Override
    public Mono<AppResponse<StateProvinceDto>> createNewStateProvince(StateProvinceRequestDto stateProvinceRequestDto) {
        return countryInternalService.findCountryByPublicId(stateProvinceRequestDto.getCountryId())
                .flatMap(country -> stateProvinceInternalService
                        .findStateProvinceByName(stateProvinceRequestDto.getName().toUpperCase())
                        .flatMap(foundStateProvince -> Mono.error(new ConflictException(messageSourceService
                                .getMessageByKey("state.province.name.already.exist.error"))))
                        .switchIfEmpty(createStateProvince(stateProvinceRequestDto, country))
                        .map(stateProvince -> {
                            StateProvinceDto stateProvinceDto = StateProvinceHelper
                                    .buildStateProvinceDto((StateProvince) stateProvince, country);

                            return new AppResponse<>(HttpStatus.CREATED.value(),
                                    messageSourceService.getMessageByKey("state.province.created.successfully"),
                                    messageSourceService.getMessageByKey("state.province.created.successfully"),
                                    stateProvinceDto, null);
                        }));
    }

    private Mono<StateProvince> createStateProvince(StateProvinceRequestDto stateProvinceRequestDto, Country country) {
        StateProvince stateProvince = StateProvinceHelper.buildStateProvince(stateProvinceRequestDto, country);
        return stateProvinceInternalService.saveStateProvince(stateProvince);
    }

    @Override
    public Mono<AppResponse<StateProvinceDto>> findStateProvinceByPublicId(UUID publicId) {
        return stateProvinceInternalService.findStateProvinceByPublicId(publicId)
                .flatMap(stateProvince -> countryInternalService.findById(stateProvince.getCountryId())
                        .map(country -> {
                            StateProvinceDto stateProvinceDto = StateProvinceHelper.buildStateProvinceDto(stateProvince,
                                    country);
                            String message = messageSourceService.getMessageByKey(MessageLog.STATE_PROVINCE_RECEIVED);
                            return new AppResponse<>(HttpStatus.OK.value(), message, message, stateProvinceDto, null);
                        }));
    }

    @Override
    public Mono<AppResponse<StateProvinceDto>> findStateProvinceByCodeAndCountryCode(String countryCode, String code) {
        return countryInternalService.findCountryByThreeLetterCode(countryCode)
                .flatMap(country -> stateProvinceInternalService.findStateProvinceByCode(code)
                        .map(stateProvince -> {
                            StateProvinceDto stateProvinceDto = StateProvinceHelper.buildStateProvinceDto(stateProvince,
                                    country);
                            String message = messageSourceService.getMessageByKey(MessageLog.STATE_PROVINCE_RECEIVED);
                            return new AppResponse<>(HttpStatus.OK.value(), message, message, stateProvinceDto, null);
                        }));
    }

    @Override
    public Mono<AppResponse<StateProvinceDto>> deactivateStateProvince(UUID publicId) {
        return stateProvinceInternalService.findStateProvinceByPublicId(publicId)
                .flatMap(stateProvince -> {
                    stateProvince.setStatus(Status.INACTIVE.name());
                    stateProvince.setLastModifiedDate(LocalDateTime.now());
                    return stateProvinceInternalService.saveStateProvince(stateProvince);
                }).flatMap(activatedProvince -> countryInternalService.findById(activatedProvince.getCountryId())
                        .map(country -> {
                            StateProvinceDto stateProvinceDto = StateProvinceHelper
                                    .buildStateProvinceDto(activatedProvince, country);
                            return new AppResponse<>(HttpStatus.OK.value(),
                                    messageSourceService.getMessageByKey("state.province.deactivated.successfully"),
                                    messageSourceService.getMessageByKey("state.province.deactivated.successfully"),
                                    stateProvinceDto, null);
                        }));
    }

    @Override
    public Mono<AppResponse<StateProvinceDto>> activateStateProvince(UUID publicId) {
        return stateProvinceInternalService.findStateProvinceByPublicId(publicId)
                .flatMap(stateProvince -> {
                    stateProvince.setStatus(Status.ACTIVE.name());
                    stateProvince.setLastModifiedDate(LocalDateTime.now());
                    return stateProvinceInternalService.saveStateProvince(stateProvince);
                }).flatMap(activatedProvince -> countryInternalService.findById(activatedProvince.getCountryId())
                        .map(country -> {
                            StateProvinceDto stateProvinceDto = StateProvinceHelper
                                    .buildStateProvinceDto(activatedProvince, country);
                            return new AppResponse<>(HttpStatus.OK.value(),
                                    messageSourceService.getMessageByKey("state.province.activated.successfully"),
                                    messageSourceService.getMessageByKey("state.province.activated.successfully"),
                                    stateProvinceDto, null);
                        }));
    }

    @Override
    public Mono<AppResponse<PageImpl<StateProvinceDto>>> findAllStateProvinces(int page, int size, String status,
            UUID countryId, String name, String sort) {
        return LocationHelper
                .filteringCheck(page, size, status, name, sort, "name",
                        messageSourceService.getMessageByKey("page.size.error"))
                .flatMap(f -> filter(f.getPageable(), countryId, f.getStatus(), f.getName()));
    }

    private Mono<AppResponse<PageImpl<StateProvinceDto>>> filter(Pageable pageable, UUID countryId, String status,
            String name) {
        var countryMono = countryInternalService.findCountryByPublicId(countryId);
        Mono<PageImpl<StateProvinceDto>> stateProvinces;

        if (name.isEmpty() && status.isEmpty()) {
            stateProvinces = getAll(countryMono, pageable);
        } else if (name.isEmpty()) {
            stateProvinces = findAllByStatus(countryMono, status, pageable);
        } else if (status.isEmpty()) {
            stateProvinces = findAllByName(countryMono, name, pageable);
        } else {
            stateProvinces = findAllByStatusAndName(countryMono, status, name, pageable);
        }

        return stateProvinces.map(result -> new AppResponse<>(HttpStatus.OK.value(),
                messageSourceService.getMessageByKey(STATE_RETRIEVED),
                messageSourceService.getMessageByKey(STATE_RETRIEVED),
                result, null));
    }

    private Mono<PageImpl<StateProvinceDto>> getAll(Mono<Country> countryMono, Pageable pageable) {
        return countryMono.flatMap(country -> {
            var stateProvinces = stateProvinceInternalService.findAll(country.getId(), pageable);
            Mono<Long> stateProvinceCount = stateProvinceInternalService.countByCountryId(country.getId());
            return stateProvinceInternalService.convertStateProvinceToPageableDto(stateProvinces, pageable,
                    stateProvinceCount);
        });
    }

    private Mono<PageImpl<StateProvinceDto>> findAllByStatus(Mono<Country> countryMono, String status,
            Pageable pageable) {
        return countryMono.flatMap(country -> {
            var stateProvinces = stateProvinceInternalService.findAllByStatus(country.getId(), status, pageable);
            Mono<Long> stateProvinceCount = stateProvinceInternalService.countByStatusAndCountryId(status,
                    country.getId());
            return stateProvinceInternalService.convertStateProvinceToPageableDto(stateProvinces, pageable,
                    stateProvinceCount);
        });
    }

    private Mono<PageImpl<StateProvinceDto>> findAllByName(Mono<Country> countryMono, String name, Pageable pageable) {
        return countryMono.flatMap(country -> {
            var stateProvinces = stateProvinceInternalService.findAllByName(country.getId(), name, pageable);
            Mono<Long> stateProvinceCount = stateProvinceInternalService.countByNameAndCountryId(country.getId(), name);
            return stateProvinceInternalService.convertStateProvinceToPageableDto(stateProvinces, pageable,
                    stateProvinceCount);
        });
    }

    private Mono<PageImpl<StateProvinceDto>> findAllByStatusAndName(Mono<Country> countryMono, String status,
            String name, Pageable pageable) {
        return countryMono.flatMap(country -> {
            var stateProvinces = stateProvinceInternalService.findAllByStatusAndName(country.getId(), status, name,
                    pageable);
            Mono<Long> stateProvinceCount = stateProvinceInternalService.countByNameAndCountryId(country.getId(), name);
            return stateProvinceInternalService.convertStateProvinceToPageableDto(stateProvinces, pageable,
                    stateProvinceCount);
        });
    }

    @Override
    public Mono<AppResponse<List<StateProvinceDto>>> findStateProvinceByStatePublicIds(List<UUID> statePublicIds) {
        return stateProvinceInternalService.findStateProvinceByPublicIds(statePublicIds)
                .flatMap(stateProvince -> Mono.just(StateProvinceHelper.buildStateProvinceDto(stateProvince)))
                .collectList().map(data -> new AppResponse<>(HttpStatus.OK.value(),
                        messageSourceService.getMessageByKey(STATE_RETRIEVED),
                        messageSourceService.getMessageByKey(STATE_RETRIEVED),
                        data, null)

                );
    }
}
