package com.mctoluene.locationservice.services.internal.impl;

import com.mctoluene.locationservice.models.Language;
import com.mctoluene.locationservice.repositories.LanguageRepository;
import com.mctoluene.locationservice.services.internal.LanguageInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctolueneam.commons.exceptions.NotFoundException;
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
public class LanguageInternalServiceImpl implements LanguageInternalService {

    private final LanguageRepository languageRepository;

    private final MessageSourceService messageSourceService;

    @Override
    public Mono<Language> saveLanguage(Language language) {
        return languageRepository.save(language)
                .onErrorResume(throwable -> {
                    log.error("An error occurred {}", throwable.getMessage());
                    if (throwable instanceof DataIntegrityViolationException) {
                        throw new ValidatorException(
                                messageSourceService.getMessageByKey("entity.data.integrity.error"));
                    }
                    throw new UnProcessableEntityException(
                            messageSourceService.getMessageByKey("language.unprocessable.error"));
                });
    }

    @Override
    public Mono<Language> findLanguageByPublicId(UUID publicId) {
        return languageRepository.findLanguageByPublicId(publicId)
                .switchIfEmpty(Mono.error(
                        new NotFoundException(messageSourceService.getMessageByKey("language.does.not.exist.error"))));
    }

    @Override
    public Flux<Language> findLanguageByPublicId(List<UUID> publicId) {
        return languageRepository.findAllByPublicIdIn(publicId);
    }

    @Override
    public Mono<Language> findLanguageByName(String languageName) {
        return languageRepository.findLanguageByNameIgnoreCase(languageName);
    }

    @Override
    public Flux<Language> findAllLanguages(Pageable pageable) {
        return languageRepository.findAllBy(pageable);
    }

    @Override
    public Mono<Long> count() {
        return languageRepository.count();
    }

}
