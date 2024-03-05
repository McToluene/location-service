package com.mctoluene.locationservice.services.main.impl;

import com.mctoluene.locationservice.domains.dtos.LocalGovernmentDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.LocalGovernmentRequestDto;
import com.mctoluene.locationservice.helpers.LocalGovernmentHelper;
import com.mctoluene.locationservice.helpers.LocationHelper;
import com.mctoluene.locationservice.models.City;
import com.mctoluene.locationservice.models.LocalGovernment;
import com.mctoluene.locationservice.services.internal.CityInternalService;
import com.mctoluene.locationservice.services.internal.LocalGovernmentInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.StateProvinceInternalService;
import com.mctoluene.locationservice.services.main.LocalGovernmentService;
import com.sabiam.commons.exceptions.ConflictException;
import com.sabiam.commons.exceptions.ValidatorException;
import com.sabiam.commons.response.AppResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalGovernmentServiceImpl implements LocalGovernmentService {

    private final StateProvinceInternalService stateProvinceInternalService;
    private final CityInternalService cityInternalService;
    private final LocalGovernmentInternalService localGovernmentInternalService;

    private final MessageSourceService messageSourceService;

    private static final String LOCAL_GOVERNMENT_RETRIEVED = "local.government.retrieved.successfully";

    @Override
    public Mono<AppResponse<LocalGovernmentDto>> createNewLocalGovernment(
            LocalGovernmentRequestDto localGovernmentRequestDto) {
        return cityInternalService.findCityByPublicId(localGovernmentRequestDto.getCityId())
                .flatMap(city -> localGovernmentInternalService
                        .findLocalGovernmentByName(
                                localGovernmentRequestDto.getName().toUpperCase())
                        .flatMap(foundLocalGovernment -> Mono
                                .error(new ConflictException(messageSourceService
                                        .getMessageByKey(
                                                "name.already.exist.error"))))
                        .switchIfEmpty(createLocalGovernment(localGovernmentRequestDto, city))
                        .map(localGovernment -> {
                            LocalGovernmentDto localGovernmentDto = LocalGovernmentHelper
                                    .buildLocalGovernmentDto(
                                            (LocalGovernment) localGovernment,
                                            city);
                            return new AppResponse<>(HttpStatus.CREATED.value(),
                                    messageSourceService.getMessageByKey(
                                            "local.government.created.successfully"),
                                    messageSourceService.getMessageByKey(
                                            "local.government.created.successfully"),
                                    localGovernmentDto, null);
                        }));
    }

    private Mono<LocalGovernment> createLocalGovernment(LocalGovernmentRequestDto localGovernmentRequestDto,
            City city) {
        LocalGovernment localGovernment = LocalGovernmentHelper.buildLocalGovernment(localGovernmentRequestDto,
                city);
        return localGovernmentInternalService.saveLocalGovernment(localGovernment);
    }

    @Override
    public Mono<AppResponse<LocalGovernmentDto>> findLocalGovernmentByPublicId(UUID publicId) {
        return localGovernmentInternalService.findLocalGovernmentByPublicId(publicId)
                .flatMap(this::getCityForLocalGovernment);
    }

    @Override
    public Mono<AppResponse<LocalGovernmentDto>> findLocalGovernmentByNameCityNameAndStateStatusCode(String name,
            String cityName, String stateCode) {
        return stateProvinceInternalService.findStateProvinceByCode(stateCode)
                .flatMap(stateProvince -> cityInternalService.findCityByNameAndState(name, stateProvince)
                        .flatMap(city -> localGovernmentInternalService.findLocalGovernmentByNameAndCity(name, city)
                                .map(localGovernment -> {
                                    LocalGovernmentDto localGovernmentDto = LocalGovernmentHelper
                                            .buildLocalGovernmentDto(localGovernment, city, stateProvince);
                                    return new AppResponse<>(HttpStatus.OK.value(),
                                            messageSourceService.getMessageByKey(LOCAL_GOVERNMENT_RETRIEVED),
                                            messageSourceService.getMessageByKey(LOCAL_GOVERNMENT_RETRIEVED),
                                            localGovernmentDto, null);
                                })));
    }

    @Override
    public Mono<AppResponse<LocalGovernmentDto>> deactivateLocalGovernment(UUID publicId) {
        return localGovernmentInternalService.findLocalGovernmentByPublicId(publicId)
                .flatMap(localGovernment -> {
                    localGovernment.setStatus(Status.INACTIVE.name());
                    localGovernment.setLastModifiedDate(LocalDateTime.now());
                    return localGovernmentInternalService.saveLocalGovernment(localGovernment);
                })
                .flatMap(deactivatedLocalGovernment -> cityInternalService
                        .findById(deactivatedLocalGovernment.getCityId())
                        .map(city -> {
                            LocalGovernmentDto localGovernmentDto = LocalGovernmentHelper
                                    .buildLocalGovernmentDto(
                                            deactivatedLocalGovernment,
                                            city);
                            return new AppResponse<>(HttpStatus.OK.value(),
                                    messageSourceService.getMessageByKey(
                                            "local.government.deactivated.successfully"),
                                    messageSourceService.getMessageByKey(
                                            "local.government.deactivated.successfully"),
                                    localGovernmentDto, null);
                        }));
    }

    @Override
    public Mono<AppResponse<LocalGovernmentDto>> activateLocalGovernment(UUID publicId) {
        return localGovernmentInternalService.findLocalGovernmentByPublicId(publicId)
                .flatMap(localGovernment -> {
                    localGovernment.setStatus(Status.ACTIVE.name());
                    localGovernment.setLastModifiedDate(LocalDateTime.now());
                    return localGovernmentInternalService.saveLocalGovernment(localGovernment);
                })
                .flatMap(deactivatedLocalGovernment -> cityInternalService
                        .findById(deactivatedLocalGovernment.getCityId())
                        .map(city -> {
                            LocalGovernmentDto localGovernmentDto = LocalGovernmentHelper
                                    .buildLocalGovernmentDto(
                                            deactivatedLocalGovernment,
                                            city);
                            return new AppResponse<>(HttpStatus.OK.value(),
                                    messageSourceService.getMessageByKey(
                                            "local.government.activated.successfully"),
                                    messageSourceService.getMessageByKey(
                                            "local.government.activated.successfully"),
                                    localGovernmentDto, null);
                        }));
    }

    @Override
    public Mono<AppResponse<PageImpl<LocalGovernmentDto>>> findAllLocalGovernment(Integer page, Integer size,
            String status, UUID cityId,
            String name,
            String sort) {
        return LocationHelper
                .filteringCheck(page, size, status, name, sort, "name",
                        messageSourceService.getMessageByKey("page.size.error"))
                .flatMap(filtering -> filter(filtering.getPageable(), cityId, filtering.getStatus(),
                        filtering.getName()));
    }

    private Mono<AppResponse<PageImpl<LocalGovernmentDto>>> filter(Pageable pageable, UUID cityId, String status,
            String name) {
        var cityMono = cityInternalService.findCityByPublicId(cityId);
        Mono<PageImpl<LocalGovernmentDto>> localGovernments;
        if (name.isEmpty() && status.isEmpty()) {
            localGovernments = findAllLocalGovernments(cityMono, pageable);
        } else if (name.isEmpty()) {
            localGovernments = findAllLocalGovernmentsByStatus(cityMono, status, pageable);
        } else if (status.isEmpty()) {
            localGovernments = findAllLocalGovernmentsByName(cityMono, name, pageable);
        } else {
            localGovernments = findAllByLocalGovernmentsStatusAndName(cityMono, status, name, pageable);
        }

        return localGovernments.map(result -> new AppResponse<>(HttpStatus.OK.value(),
                messageSourceService.getMessageByKey(LOCAL_GOVERNMENT_RETRIEVED),
                messageSourceService.getMessageByKey(LOCAL_GOVERNMENT_RETRIEVED), result, null));
    }

    private Mono<PageImpl<LocalGovernmentDto>> findAllLocalGovernments(Mono<City> cityMono, Pageable pageable) {
        return cityMono.flatMap(city -> {
            var localGovernments = localGovernmentInternalService.findAll(city.getId(), pageable);
            Mono<Long> count = localGovernmentInternalService.countByCityId(city.getId());
            return localGovernmentInternalService.convertLocalGovernmentToPageableDto(localGovernments,
                    pageable, city,
                    count);
        });
    }

    private Mono<PageImpl<LocalGovernmentDto>> findAllLocalGovernmentsByStatus(Mono<City> cityMono, String status,
            Pageable pageable) {
        return cityMono.flatMap(city -> {
            var localGovernments = localGovernmentInternalService.findAllByStatus(city.getId(), status,
                    pageable);
            Mono<Long> count = localGovernmentInternalService.countByStatusAndCityId(status, city.getId());
            return localGovernmentInternalService.convertLocalGovernmentToPageableDto(localGovernments,
                    pageable, city,
                    count);
        });
    }

    private Mono<PageImpl<LocalGovernmentDto>> findAllLocalGovernmentsByName(Mono<City> cityMono, String name,
            Pageable pageable) {
        return cityMono.flatMap(city -> {
            var localGovernments = localGovernmentInternalService.findAllByName(city.getId(), name,
                    pageable);
            Mono<Long> count = localGovernmentInternalService.countByNameAndCityId(city.getId(), name);
            return localGovernmentInternalService.convertLocalGovernmentToPageableDto(localGovernments,
                    pageable, city,
                    count);
        });
    }

    private Mono<PageImpl<LocalGovernmentDto>> findAllByLocalGovernmentsStatusAndName(Mono<City> cityMono,
            String status, String name, Pageable pageable) {
        return cityMono.flatMap(city -> {
            var localGovernments = localGovernmentInternalService.findAllByStatusAndName(city.getId(),
                    status, name,
                    pageable);
            Mono<Long> count = localGovernmentInternalService.countByNameAndStatusAndCityId(city.getId(),
                    status, name);
            return localGovernmentInternalService.convertLocalGovernmentToPageableDto(localGovernments,
                    pageable, city,
                    count);
        });
    }

    @Override
    public Mono<AppResponse<List<LocalGovernmentDto>>> findLocalGovernmentByPublicIds(List<UUID> publicIds) {
        if (publicIds == null)
            return Mono.error(new ValidatorException("publicIds cannot be null"));

        publicIds = publicIds.stream().distinct().toList();
        return localGovernmentInternalService.findLocalGovernmentByPublicIds(publicIds)
                .flatMap(localGovernment -> cityInternalService.findById(localGovernment.getCityId())
                        .flatMap(city -> stateProvinceInternalService.findById(city.getStateProvinceId())
                                .map(state -> LocalGovernmentHelper.buildLocalGovernmentDto(localGovernment, city,
                                        state))))
                .collectList()
                .map(localGovernments -> new AppResponse<>(
                        HttpStatus.OK.value(),
                        messageSourceService.getMessageByKey(LOCAL_GOVERNMENT_RETRIEVED),
                        messageSourceService.getMessageByKey(LOCAL_GOVERNMENT_RETRIEVED),
                        localGovernments,
                        null));
    }

    private Mono<? extends AppResponse<LocalGovernmentDto>> getCityForLocalGovernment(LocalGovernment localGovernment) {
        return cityInternalService.findById(localGovernment.getCityId())
                .flatMap(city -> stateProvinceInternalService
                        .findById(city.getStateProvinceId())
                        .map(state -> {
                            LocalGovernmentDto localGovernmentDto = LocalGovernmentHelper
                                    .buildLocalGovernmentDto(localGovernment, city, state);
                            return new AppResponse<>(HttpStatus.OK.value(),
                                    messageSourceService.getMessageByKey(LOCAL_GOVERNMENT_RETRIEVED),
                                    messageSourceService.getMessageByKey(LOCAL_GOVERNMENT_RETRIEVED),
                                    localGovernmentDto, null);
                        }));
    }
}
