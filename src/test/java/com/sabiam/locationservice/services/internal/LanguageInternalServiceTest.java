package com.mctolueneam.locationservice.services.internal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mctoluene.locationservice.models.Language;
import com.mctoluene.locationservice.repositories.LanguageRepository;
import com.mctoluene.locationservice.services.internal.LanguageInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.impl.LanguageInternalServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

class LanguageInternalServiceTest {

    @Mock
    LanguageRepository languageRepository;

    @Mock
    MessageSourceService messageSourceService;

    LanguageInternalService languageInternalService;

    Language language;

    Language savedLanguage;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        languageInternalService = new LanguageInternalServiceImpl(languageRepository, messageSourceService);
        language = buildLanguageEntity();
        savedLanguage = saveLanguage(language);
    }

    @Test
    void saveLanguage() {
        when(languageRepository.save(any())).thenReturn(Mono.just(savedLanguage));
        var savedLanguage = languageInternalService.saveLanguage(language);

        StepVerifier.create(savedLanguage)
                .consumeNextWith(language -> {
                    assertThat(language).isNotNull();
                    assertThat(language.getName()).isNotBlank();
                    assertThat(language.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findAllLanguages() {
        Pageable pageable = PageRequest.of(0, 1);
        given(languageRepository.findAllBy(pageable)).willReturn(Flux.just(savedLanguage));
        given(languageInternalService.findAllLanguages(pageable)).willReturn(Flux.just(savedLanguage));

        var allLanguages = languageInternalService.findAllLanguages(pageable);
        StepVerifier.create(allLanguages)
                .expectNext(savedLanguage)
                .expectComplete()
                .verify();
    }

    @Test
    void findLanguageByPublicId() {
        when(languageRepository.findLanguageByPublicId(any())).thenReturn(Mono.just(language));
        var foundLanguage = languageInternalService.findLanguageByPublicId(language.getPublicId());

        StepVerifier.create(foundLanguage)
                .consumeNextWith(language -> {
                    assertThat(language).isNotNull();
                    assertThat(language.getName()).isNotBlank();
                    assertThat(language.getId()).isNotNull();
                })
                .verifyComplete();
    }

    Language buildLanguageEntity() {
        Language language = Language.builder()
                .name("Soti")
                .publicId(UUID.randomUUID())
                .build();

        language.setCreatedBy("SYSTEM");
        language.setCreatedDate(LocalDateTime.now());
        return language;
    }

    Language saveLanguage(Language language) {
        language.setId(UUID.randomUUID());
        language.setCreatedDate(LocalDateTime.now());
        return language;
    }
}