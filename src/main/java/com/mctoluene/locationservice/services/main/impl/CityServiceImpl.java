package com.mctoluene.locationservice.services.main.impl;

import com.mctoluene.locationservice.domains.dtos.CityDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.CityRequestDto;
import com.mctoluene.locationservice.helpers.CityHelper;
import com.mctoluene.locationservice.helpers.LocationHelper;
import com.mctoluene.locationservice.models.City;
import com.mctoluene.locationservice.models.StateProvince;
import com.mctoluene.locationservice.services.internal.CityInternalService;
import com.mctoluene.locationservice.services.internal.CountryInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.StateProvinceInternalService;
import com.mctoluene.locationservice.services.main.CityService;
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
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CountryInternalService countryInternalService;
    private final CityInternalService cityInternalService;

    private final StateProvinceInternalService stateProvinceInternalService;

    private final MessageSourceService messageSourceService;

    private static final String CITY_RETRIEVED = "city.retrieved.successfully";

    @Override
    public Mono<AppResponse<CityDto>> createNewCity(CityRequestDto cityRequestDto) {

        return stateProvinceInternalService.findStateProvinceByPublicId(cityRequestDto.getStateProvinceId())
                .flatMap(stateProvince -> cityInternalService.findCityByName(cityRequestDto.getName().toUpperCase())
                        .flatMap(foundStateProvince -> Mono.error(new ConflictException(messageSourceService
                                .getMessageByKey("city.name.already.exist.error"))))
                        .switchIfEmpty(createCity(cityRequestDto, stateProvince))
                        .map(city -> {
                            CityDto cityDto = CityHelper.buildCityDto((City) city, stateProvince);
                            return new AppResponse<>(HttpStatus.CREATED.value(),
                                    messageSourceService.getMessageByKey("city.created.successfully"),
                                    messageSourceService.getMessageByKey("city.created.successfully"),
                                    cityDto, null);
                        }));
    }

    private Mono<City> createCity(CityRequestDto cityRequestDto, StateProvince stateProvince) {
        City city = CityHelper.buildCity(cityRequestDto, stateProvince);
        return cityInternalService.saveCity(city);
    }

    @Override
    public Mono<AppResponse<CityDto>> findCityByPublicId(UUID publicId) {
        return cityInternalService.findCityByPublicId(publicId)
                .flatMap(city -> stateProvinceInternalService.findById(city.getStateProvinceId())
                        .map(stateProvince -> {
                            CityDto cityDto = CityHelper.buildCityDto(city, stateProvince);

                            return new AppResponse<>(HttpStatus.OK.value(),
                                    messageSourceService.getMessageByKey(CITY_RETRIEVED),
                                    messageSourceService.getMessageByKey(CITY_RETRIEVED),
                                    cityDto, null);
                        }));
    }

    @Override
    public Mono<AppResponse<CityDto>> findCityByNameStateCodeAndCountryCode(String name, String countryCode,
            String stateCode) {
        return countryInternalService.findCountryByThreeLetterCode(countryCode)
                .flatMap(country -> stateProvinceInternalService.findStateProvinceByCode(stateCode)
                        .flatMap(stateProvince -> cityInternalService.findCityByNameAndState(name, stateProvince)
                                .map(city -> {
                                    CityDto cityDto = CityHelper.buildCityDto(city, stateProvince);
                                    return new AppResponse<>(HttpStatus.OK.value(),
                                            messageSourceService.getMessageByKey(CITY_RETRIEVED),
                                            messageSourceService.getMessageByKey(CITY_RETRIEVED),
                                            cityDto, null);
                                })));
    }

    @Override
    public Mono<AppResponse<CityDto>> deactivateCity(UUID publicId) {
        return cityInternalService.findCityByPublicId(publicId)
                .flatMap(city -> {
                    city.setStatus(Status.INACTIVE.name());
                    city.setLastModifiedDate(LocalDateTime.now());
                    return cityInternalService.saveCity(city);
                })
                .flatMap(deactivatedCity -> stateProvinceInternalService.findById(deactivatedCity.getStateProvinceId())
                        .map(stateProvince -> {
                            CityDto cityDto = CityHelper.buildCityDto(deactivatedCity, stateProvince);
                            return new AppResponse<>(HttpStatus.OK.value(),
                                    messageSourceService.getMessageByKey("city.deactivated.successfully"),
                                    messageSourceService.getMessageByKey("city.deactivated.successfully"),
                                    cityDto, null);
                        }));
    }

    @Override
    public Mono<AppResponse<CityDto>> activateCity(UUID publicId) {
        return cityInternalService.findCityByPublicId(publicId)
                .flatMap(city -> {
                    city.setStatus(Status.ACTIVE.name());
                    city.setLastModifiedDate(LocalDateTime.now());
                    return cityInternalService.saveCity(city);
                })
                .flatMap(deactivatedCity -> stateProvinceInternalService.findById(deactivatedCity.getStateProvinceId())
                        .map(stateProvince -> {
                            CityDto cityDto = CityHelper.buildCityDto(deactivatedCity, stateProvince);
                            return new AppResponse<>(HttpStatus.OK.value(),
                                    messageSourceService.getMessageByKey("city.activated.successfully"),
                                    messageSourceService.getMessageByKey("city.activated.successfully"),
                                    cityDto, null);
                        }));
    }

    @Override
    public Mono<AppResponse<PageImpl<CityDto>>> findAllCities(int page, int size, String status, UUID stateProvinceId,
            String name, String sort) {
        return LocationHelper
                .filteringCheck(page, size, status, name, sort, "name",
                        messageSourceService.getMessageByKey("page.size.error"))
                .flatMap(filtering -> filter(filtering.getPageable(), stateProvinceId, filtering.getStatus(),
                        filtering.getName()));
    }

    private Mono<AppResponse<PageImpl<CityDto>>> filter(Pageable pageable, UUID stateProvinceId, String status,
            String name) {
        var stateProvinceMono = stateProvinceInternalService.findStateProvinceByPublicId(stateProvinceId);
        Mono<PageImpl<CityDto>> cities;
        if (name.isEmpty() && status.isEmpty()) {
            cities = findAllCities(stateProvinceMono, pageable);
        } else if (name.isEmpty()) {
            cities = findAllCitiesByStatus(stateProvinceMono, status, pageable);
        } else if (status.isEmpty()) {
            cities = findAllCitiesByName(stateProvinceMono, name, pageable);
        } else {
            cities = findAllByCitiesStatusAndName(stateProvinceMono, status, name, pageable);
        }

        return cities.map(result -> new AppResponse<>(HttpStatus.OK.value(),
                messageSourceService.getMessageByKey(CITY_RETRIEVED),
                messageSourceService.getMessageByKey(CITY_RETRIEVED),
                result, null));
    }

    private Mono<PageImpl<CityDto>> findAllCities(Mono<StateProvince> stateProvinceMono, Pageable pageable) {
        return stateProvinceMono.flatMap(stateProvince -> {
            var localGovernments = cityInternalService.findAll(stateProvince.getId(), pageable);
            Mono<Long> count = cityInternalService.countByStateProvinceId(stateProvince.getId());
            return cityInternalService.convertCityToPageableDto(localGovernments, pageable, count);
        });
    }

    private Mono<PageImpl<CityDto>> findAllCitiesByStatus(Mono<StateProvince> stateProvinceMono, String status,
            Pageable pageable) {
        return stateProvinceMono.flatMap(stateProvince -> {
            var localGovernments = cityInternalService.findAllByStatus(stateProvince.getId(), status, pageable);
            Mono<Long> count = cityInternalService.countByStatusAndStateProvinceId(status, stateProvince.getId());
            return cityInternalService.convertCityToPageableDto(localGovernments, pageable, count);
        });
    }

    private Mono<PageImpl<CityDto>> findAllCitiesByName(Mono<StateProvince> stateProvinceMono, String name,
            Pageable pageable) {
        return stateProvinceMono.flatMap(stateProvince -> {
            var localGovernments = cityInternalService.findAllByName(stateProvince.getId(), name, pageable);
            Mono<Long> count = cityInternalService.countByNameAndStateProvinceId(stateProvince.getId(), name);
            return cityInternalService.convertCityToPageableDto(localGovernments, pageable, count);
        });
    }

    private Mono<PageImpl<CityDto>> findAllByCitiesStatusAndName(Mono<StateProvince> stateProvinceMono, String status,
            String name, Pageable pageable) {
        return stateProvinceMono.flatMap(stateProvince -> {
            var localGovernments = cityInternalService.findAllByStatusAndName(stateProvince.getId(), status, name,
                    pageable);
            Mono<Long> count = cityInternalService.countByNameAndStatusAndStateProvinceId(stateProvince.getId(), status,
                    name);
            return cityInternalService.convertCityToPageableDto(localGovernments, pageable, count);
        });
    }
}
