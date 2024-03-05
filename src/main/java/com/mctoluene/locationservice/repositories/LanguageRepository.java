package com.mctoluene.locationservice.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.mctoluene.locationservice.models.Language;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public interface LanguageRepository extends R2dbcRepository<Language, UUID> {

    Mono<Language> findLanguageByPublicId(UUID publicId);

    Flux<Language> findAllBy(Pageable pageable);

    Mono<Language> findLanguageByNameIgnoreCase(String languageName);

    Flux<Language> findAllByPublicIdIn(List<UUID> publicId);
}
