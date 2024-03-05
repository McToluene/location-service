package com.mctoluene.locationservice.services.internal.impl;

import com.mctoluene.locationservice.domains.dtos.LocalGovernmentDto;
import com.mctoluene.locationservice.helpers.LocalGovernmentHelper;
import com.mctoluene.locationservice.models.City;
import com.mctoluene.locationservice.models.LocalGovernment;
import com.mctoluene.locationservice.repositories.LocalGovernmentRepository;
import com.mctoluene.locationservice.services.internal.LocalGovernmentInternalService;
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

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocalGovernmentInternalServiceImpl implements LocalGovernmentInternalService {

    private final LocalGovernmentRepository localGovernmentRepository;

    private final MessageSourceService messageSourceService;
    private static final String NOT_FOUND_MESSAGE_KEY = "local.government.does.not.exist.error";

    @Override
    public Mono<LocalGovernment> saveLocalGovernment(LocalGovernment localGovernment) {
        return localGovernmentRepository.save(localGovernment)
                .onErrorResume(throwable -> {
                    log.error("An error occurred {}", throwable.getMessage());
                    if (throwable instanceof DataIntegrityViolationException) {
                        throw new ValidatorException(
                                messageSourceService.getMessageByKey("entity.data.integrity.error"));
                    }
                    throw new UnProcessableEntityException(
                            messageSourceService.getMessageByKey("local.government.unprocessable.error"));
                });
    }

    @Override
    public Mono<LocalGovernment> findLocalGovernmentByPublicId(UUID publicId) {
        return localGovernmentRepository.findByPublicId(publicId)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(NOT_FOUND_MESSAGE_KEY))));
    }

    @Override
    public Mono<LocalGovernment> findLocalGovernmentByPublicIdAndStatus(UUID publicId, String status) {
        return localGovernmentRepository.findByPublicIdAndStatus(publicId, status)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(NOT_FOUND_MESSAGE_KEY))));
    }

    @Override
    public Mono<LocalGovernment> findLocalGovernmentByName(String localGovernmentName) {
        return localGovernmentRepository.findLocalGovernmentByName(localGovernmentName);
    }

    @Override
    public Mono<LocalGovernment> findLocalGovernmentByNameAndCity(String localGovernmentName, City city) {
        return localGovernmentRepository.findByNameAndCityId(localGovernmentName, city.getId())
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(NOT_FOUND_MESSAGE_KEY))));
    }

    @Override
    public Flux<LocalGovernment> findLocalGovernmentByCity(Pageable pageable, City city) {
        return this.localGovernmentRepository.findAllByCityId(city.getId(), pageable);
    }

    @Override
    public Mono<PageImpl<LocalGovernmentDto>> convertLocalGovernmentToPageableDto(
            Flux<LocalGovernment> localGovernmentFlux, Pageable pageable, City city, Mono<Long> localGovernmentCount) {
        return localGovernmentFlux
                .map(localGovernment -> LocalGovernmentHelper.buildLocalGovernmentDto(localGovernment, city))
                .collectList()
                .zipWith(localGovernmentCount)
                .map(t -> new PageImpl<>(t.getT1(), pageable, t.getT2()));
    }

    @Override
    public Mono<Long> countByStatusAndCityId(String status, UUID cityId) {
        return localGovernmentRepository.countByStatusAndCityId(status.toUpperCase(), cityId);
    }

    @Override
    public Flux<LocalGovernment> findAll(UUID cityId, Pageable pageable) {
        return localGovernmentRepository.findAllByCityId(cityId, pageable);
    }

    @Override
    public Mono<Long> countByCityId(UUID cityId) {
        return localGovernmentRepository.countByCityId(cityId);
    }

    @Override
    public Flux<LocalGovernment> findAllByStatus(UUID cityId, String status, Pageable pageable) {
        return localGovernmentRepository.findAllByStatusAndCityId(status, cityId, pageable);
    }

    @Override
    public Flux<LocalGovernment> findAllByName(UUID cityId, String name, Pageable pageable) {
        return localGovernmentRepository.findAllByNameContainingIgnoreCaseAndCityId(name, cityId, pageable);
    }

    @Override
    public Mono<Long> countByNameAndCityId(UUID cityId, String name) {
        return localGovernmentRepository.countByNameContainingIgnoreCaseAndCityId(name, cityId);
    }

    @Override
    public Flux<LocalGovernment> findAllByStatusAndName(UUID cityId, String status, String name, Pageable pageable) {
        return localGovernmentRepository.findAllByNameContainingIgnoreCaseAndStatusAndCityId(name, status, cityId,
                pageable);
    }

    @Override
    public Mono<Long> countByNameAndStatusAndCityId(UUID cityId, String status, String name) {
        return localGovernmentRepository.countByStatusAndNameContainingIgnoreCaseAndCityId(status, name, cityId);
    }

    @Override
    public Flux<LocalGovernment> findLocalGovernmentByPublicIds(List<UUID> publicIds) {
        return localGovernmentRepository.findByPublicIdIn(publicIds)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(NOT_FOUND_MESSAGE_KEY))));
    }
}
