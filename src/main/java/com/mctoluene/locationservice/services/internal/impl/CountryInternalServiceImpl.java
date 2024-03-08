package com.mctoluene.locationservice.services.internal.impl;

import com.mctoluene.locationservice.constants.MessageLog;
import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.exceptions.InternalServerException;
import com.mctoluene.locationservice.helpers.CountryHelper;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.repositories.CountryRepository;
import com.mctoluene.locationservice.services.internal.CountryInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctolueneam.commons.exceptions.NotFoundException;
import com.mctolueneam.commons.exceptions.UnProcessableEntityException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CountryInternalServiceImpl implements CountryInternalService {
    private final CountryRepository countryRepository;

    private final MessageSourceService messageSourceService;

    @Override
    public Mono<Country> saveCountry(Country country) {
        return countryRepository.save(country)
                .onErrorResume(throwable -> {
                    log.error("An error occurred {}", throwable.getMessage());
                    if (throwable instanceof DataIntegrityViolationException) {
                        throw new InternalServerException(
                                messageSourceService.getMessageByKey("country.unprocessable.error"));
                    }
                    throw new UnProcessableEntityException(
                            messageSourceService.getMessageByKey("country.unprocessable.error"));
                });
    }

    @Override
    public Mono<Country> findCountryByPublicId(UUID publicId) {
        return countryRepository.findByPublicId(publicId)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(MessageLog.COUNTRY_NOT_FOUND))));
    }

    @Override
    public Mono<Country> findCountryByPublicIdAndStatus(UUID publicId, String status) {
        return countryRepository.findByPublicIdAndStatus(publicId, status)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(MessageLog.COUNTRY_NOT_FOUND))));
    }

    @Override
    public Mono<Country> findCountryByDialingCode(String dialingCode) {
        return countryRepository.findCountryByDialingCode(dialingCode)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(MessageLog.COUNTRY_NOT_FOUND))));
    }

    @Override
    public Mono<Country> findCountryByTwoLetterCode(String twoLetterCode) {
        return countryRepository.findCountryByTwoLetterCode(twoLetterCode)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(MessageLog.COUNTRY_NOT_FOUND))));
    }

    @Override
    public Mono<Country> findCountryByThreeLetterCode(String threeLetterCode) {
        return countryRepository.findCountryByThreeLetterCode(threeLetterCode)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey(MessageLog.COUNTRY_NOT_FOUND))));
    }

    @Override
    public Mono<Country> findCountryByName(String countryName) {
        return countryRepository.findCountryByCountryName(countryName);
    }

    @Override
    public Flux<Country> findAllByName(String countryName, Pageable pageable) {
        return this.countryRepository.findAllByCountryNameContainingIgnoreCase(countryName, pageable);
    }

    @Override
    public Flux<Country> findAllByStatus(String status, Pageable pageable) {
        return this.countryRepository.findAllByStatus(status, pageable);
    }

    @Override
    public Flux<Country> findAllByStatusAndName(String status, String countryName, Pageable pageable) {
        return this.countryRepository.findAllByStatusAndCountryNameContainingIgnoreCase(status, countryName, pageable);
    }

    @Override
    public Flux<Country> findAllCountry(Pageable pageable) {
        return this.countryRepository.findAllBy(pageable);
    }

    @Override
    public Mono<PageImpl<CountryDto>> convertCountryToPageable(Flux<Country> countries, Pageable pageable,
            Mono<Long> countryCount) {
        return countries
                .map(CountryHelper::buildCountryDto)
                .collectList()
                .zipWith(countryCount)
                .map(t -> {
                    Pageable finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
                    return new PageImpl<>(t.getT1(), finalPageable, t.getT2());
                });
    }

    @Override
    public Mono<Country> findById(UUID countryId) {
        return countryRepository.findById(countryId)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey("country.does.exist.error"))));
    }

    @Override
    public Mono<Long> countByStatusAndName(String status, String name) {
        return countryRepository.countByStatusAndCountryNameContainingIgnoreCase(status, name);
    }

    @Override
    public Mono<Long> count() {
        return countryRepository.count();
    }

    @Override
    public Mono<Long> countByStatus(String status) {
        return countryRepository.countByStatus(status);
    }

    @Override
    public Mono<Long> countByName(String name) {
        return countryRepository.countByCountryNameContainingIgnoreCase(name.toUpperCase());
    }

    @Override
    public Flux<Country> findCountryByPublicIds(List<UUID> ids) {
        return countryRepository.findByPublicIdIn(ids)
                .switchIfEmpty(Mono.error(new NotFoundException(messageSourceService
                        .getMessageByKey("country.does.exist.error"))));
    }
}
