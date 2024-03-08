package com.mctoluene.locationservice.services.internal.impl;

import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.models.Currency;
import com.mctoluene.locationservice.repositories.CurrencyRepository;
import com.mctoluene.locationservice.services.internal.CurrencyInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctolueneam.commons.exceptions.UnProcessableEntityException;
import com.mctolueneam.commons.exceptions.ValidatorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyInternalServiceImpl implements CurrencyInternalService {

    private final CurrencyRepository currencyRepository;
    private final MessageSourceService messageSourceService;

    public Mono<Currency> saveCurrency(Currency currency) {
        return currencyRepository.save(currency)
                .onErrorResume(throwable -> {
                    log.error("An error occurred {}", throwable.getMessage());
                    if (throwable instanceof DataIntegrityViolationException) {
                        throw new ValidatorException(
                                messageSourceService.getMessageByKey("entity.data.integrity.error"));
                    }
                    throw new UnProcessableEntityException(
                            messageSourceService.getMessageByKey("currency.unprocessable.error"));
                });
    }

    public Mono<Currency> findByName(String name) {
        return currencyRepository.findByNameIgnoreCase(name);
    }

    public Flux<Currency> getAllCurrencies(Pageable pageable) {
        return currencyRepository.findAllByStatus(pageable, Status.ACTIVE.name());
    }

    @Override
    public Mono<Long> countByStatus() {
        return currencyRepository.countByStatus(Status.ACTIVE.name());
    }

    @Override
    public Flux<Currency> findByPublicId(List<UUID> publicId) {
        return currencyRepository.findAllByPublicIdIn(publicId);
    }
}