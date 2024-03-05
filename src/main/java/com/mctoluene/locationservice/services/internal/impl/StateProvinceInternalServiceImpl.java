package com.mctoluene.locationservice.services.internal.impl;

import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.helpers.StateProvinceHelper;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.models.StateProvince;
import com.mctoluene.locationservice.repositories.StateProvinceRepository;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.StateProvinceInternalService;
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

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StateProvinceInternalServiceImpl implements StateProvinceInternalService {

    private final StateProvinceRepository stateProvinceRepository;

    private final MessageSourceService messageSourceService;
    private static final String ERROR_KEY = "state.province.does.not.exist.error";

    @Override
    public Mono<StateProvince> saveStateProvince(StateProvince stateProvince) {
        return stateProvinceRepository.save(stateProvince)
                .onErrorResume(throwable -> {
                    log.error("An error occurred {}", throwable.getMessage());
                    if (throwable instanceof DataIntegrityViolationException) {
                        throw new ValidatorException(
                                messageSourceService.getMessageByKey("entity.data.integrity.error"));
                    }
                    throw new UnProcessableEntityException(
                            messageSourceService.getMessageByKey("state.province.unprocessable.error"));
                });
    }

    @Override
    public Mono<StateProvince> findStateProvinceByPublicId(UUID publicId) {
        return stateProvinceRepository.findByPublicId(publicId)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(ERROR_KEY))));
    }

    @Override
    public Mono<StateProvince> findStateProvinceByCode(String code) {
        return stateProvinceRepository.findByCode(code)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(ERROR_KEY))));
    }

    @Override
    public Mono<StateProvince> findStateProvinceByName(String stateName) {
        return stateProvinceRepository.findStateProvinceByName(stateName);
    }

    @Override
    public Flux<StateProvince> findStateProvinceByCountry(Pageable pageable, Country country) {
        return this.stateProvinceRepository.findAllByCountryId(pageable, country.getId());
    }

    @Override
    public Flux<StateProvince> findStateProvinceByCountryAndStatus(Country country, String status) {
        return this.stateProvinceRepository.findAllByCountryIdAndStatus(country.getId(), status);
    }

    public Mono<PageImpl<StateProvinceDto>> convertStateProvinceToPageableDto(Flux<StateProvince> stateProvinces,
            Pageable pageable, Mono<Long> stateProvinceCount) {
        return stateProvinces
                .map(StateProvinceHelper::buildStateProvinceDto)
                .collectList()
                .zipWith(stateProvinceCount)
                .map(t -> new PageImpl<>(t.getT1(), pageable, t.getT2()));
    }

    public Mono<StateProvince> findById(UUID stateProvinceId) {
        return stateProvinceRepository.findById(stateProvinceId)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(ERROR_KEY))));
    }

    @Override
    public Flux<StateProvince> findAll(UUID countryId, Pageable pageable) {
        return stateProvinceRepository.findAllByCountryId(pageable, countryId);
    }

    @Override
    public Flux<StateProvince> findAllByName(UUID countryId, String stateName, Pageable pageable) {
        return stateProvinceRepository.findAllByNameContainingIgnoreCaseAndCountryId(stateName, countryId, pageable);
    }

    @Override
    public Flux<StateProvince> findAllByStatus(UUID countryId, String status, Pageable pageable) {
        return stateProvinceRepository.findAllByStatusAndCountryId(status, countryId, pageable);
    }

    @Override
    public Flux<StateProvince> findAllByStatusAndName(UUID countryId, String status, String stateName,
            Pageable pageable) {
        return stateProvinceRepository.findAllByNameContainingIgnoreCaseAndStatusAndCountryId(stateName, status,
                countryId, pageable);
    }

    @Override
    public Mono<Long> countByCountryId(UUID countryId) {
        return stateProvinceRepository.countByCountryId(countryId);
    }

    @Override
    public Mono<Long> countByNameAndCountryId(UUID countryId, String name) {
        return stateProvinceRepository.countByNameContainingIgnoreCaseAndCountryId(name, countryId);
    }

    public Mono<Long> countByStatusAndCountryId(String status, UUID countryId) {
        return stateProvinceRepository.countByStatusAndCountryId(status, countryId);
    }

    @Override
    public Flux<StateProvince> findStateProvinceByPublicIds(List<UUID> statePublicIds) {
        return stateProvinceRepository.findAllByPublicIdIn(statePublicIds)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(ERROR_KEY))));
    }
}
