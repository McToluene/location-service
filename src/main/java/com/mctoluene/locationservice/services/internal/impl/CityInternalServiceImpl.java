package com.mctoluene.locationservice.services.internal.impl;

import com.mctoluene.locationservice.domains.dtos.CityDto;
import com.mctoluene.locationservice.helpers.CityHelper;
import com.mctoluene.locationservice.models.City;
import com.mctoluene.locationservice.models.StateProvince;
import com.mctoluene.locationservice.repositories.CityRepository;
import com.mctoluene.locationservice.services.internal.CityInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.sabiam.commons.exceptions.NotFoundException;
import com.sabiam.commons.exceptions.UnProcessableEntityException;
import com.sabiam.commons.exceptions.ValidatorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityInternalServiceImpl implements CityInternalService {

    private final CityRepository cityRepository;

    private final MessageSourceService messageSourceService;

    private static final String CITY_ERROR_MESSAGE = "city.does.not.exist.error";

    @Override
    public Mono<City> saveCity(City city) {
        return cityRepository.save(city)
                .onErrorResume(throwable -> {
                    log.error("An error occurred {}", throwable.getMessage());
                    if (throwable instanceof DataIntegrityViolationException) {
                        throw new ValidatorException(
                                messageSourceService.getMessageByKey("entity.data.integrity.error"));
                    }
                    throw new UnProcessableEntityException(
                            messageSourceService.getMessageByKey("city.unprocessable.error"));
                });
    }

    @Override
    public Mono<City> findCityByPublicId(UUID publicId) {
        return cityRepository.findByPublicId(publicId)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(CITY_ERROR_MESSAGE))));
    }

    @Override
    public Mono<City> findCityByNameAndState(String name, StateProvince stateProvince) {
        return cityRepository.findCityByNameAndStateProvinceId(name, stateProvince.getId())
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(CITY_ERROR_MESSAGE))));
    }

    @Override
    public Mono<City> findCityByName(String cityName) {
        return cityRepository.findCityByName(cityName);
    }

    @Override
    public Flux<City> findCityByStateProvince(Pageable pageable, StateProvince stateProvince) {
        return this.cityRepository.findAllByStateProvinceId(stateProvince.getId(), pageable);
    }

    @Override
    public Mono<PageImpl<CityDto>> convertCityToPageableDto(Flux<City> cities, Pageable pageable,
            Mono<Long> cityCount) {
        return cities
                .map(CityHelper::buildCityDto)
                .collectList()
                .zipWith(cityCount)
                .map(t -> new PageImpl<>(t.getT1(), pageable, t.getT2()));
    }

    @Override
    public Mono<City> findById(UUID cityId) {
        return cityRepository.findById(cityId)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey("city.does.not.exist.error"))));
    }

    @Override
    public Mono<Long> countByStatusAndStateProvinceId(String status, UUID stateProvinceId) {
        return cityRepository.countByStatusAndStateProvinceId(status.toUpperCase(), stateProvinceId);
    }

    @Override
    public Flux<City> findAll(UUID stateProvinceId, Pageable pageable) {
        return cityRepository.findAllByStateProvinceId(stateProvinceId, pageable);
    }

    @Override
    public Flux<City> findAllByStatus(UUID stateProvinceId, String status, Pageable pageable) {
        return cityRepository.findAllByStatusAndStateProvinceId(status, stateProvinceId, pageable);
    }

    @Override
    public Flux<City> findAllByName(UUID stateProvinceId, String name, Pageable pageable) {
        return cityRepository.findAllByNameContainingIgnoreCaseAndStateProvinceId(name, stateProvinceId, pageable);
    }

    @Override
    public Mono<Long> countByStateProvinceId(UUID stateProvinceId) {
        return cityRepository.countByStateProvinceId(stateProvinceId);
    }

    @Override
    public Flux<City> findAllByStatusAndName(UUID stateProvinceId, String status, String name, Pageable pageable) {
        return cityRepository.findAllByNameContainingIgnoreCaseAndStatusAndStateProvinceId(name, status,
                stateProvinceId, pageable);
    }

    @Override
    public Mono<Long> countByNameAndStateProvinceId(UUID stateProvinceId, String name) {
        return cityRepository.countByNameContainingIgnoreCaseAndStateProvinceId(name, stateProvinceId);
    }

    @Override
    public Mono<Long> countByNameAndStatusAndStateProvinceId(UUID stateProvinceId, String status, String name) {
        return cityRepository.countByStatusAndNameContainingIgnoreCaseAndStateProvinceId(status, name, stateProvinceId);
    }
}
