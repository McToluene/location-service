package com.mctoluene.locationservice.services.internal;

import org.springframework.data.domain.Pageable;

import com.mctoluene.locationservice.models.Language;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface LanguageInternalService {
    Mono<Language> saveLanguage(Language language);

    Mono<Language> findLanguageByPublicId(UUID publicId);

    Flux<Language> findLanguageByPublicId(List<UUID> publicId);

    Mono<Language> findLanguageByName(String languageName);

    Flux<Language> findAllLanguages(Pageable pageable);

    Mono<Long> count();
}
