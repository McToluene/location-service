package com.mctolueneam.locationservice.services.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.mctoluene.locationservice.domains.dtos.LanguageDto;
import com.mctoluene.locationservice.domains.requestdtos.LanguageRequestDto;
import com.mctoluene.locationservice.models.Language;
import com.mctoluene.locationservice.services.internal.LanguageInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.main.LanguageService;
import com.mctoluene.locationservice.services.main.impl.LanguageServiceImpl;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class LanguageServiceTest {

    @Mock
    LanguageInternalService languageInternalService;

    @Mock
    MessageSourceService messageSourceService;

    LanguageService languageService;

    LanguageRequestDto languageRequestDto;

    Language language;

    Language savedLanguage;

    AutoCloseable autoCloseable;

    LanguageDto languageDto;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        languageService = new LanguageServiceImpl(languageInternalService, messageSourceService);
        languageRequestDto = buildLanguageRequestDto();
        language = buildLanguageEntity();
        savedLanguage = saveLanguage(language);
        languageDto = buildLanguageDto(language);
    }

    @Test
    void createNewLanguage() {
        when(languageInternalService.findLanguageByName(anyString())).thenReturn(Mono.empty());
        when(languageInternalService.saveLanguage(any())).thenReturn(Mono.just(savedLanguage));

        var result = languageService.createNewLanguage(languageRequestDto);
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
                })
                .verifyComplete();

    }

    @Test
    void findLanguageByPublicId() {

        when(languageInternalService.findLanguageByPublicId(languageDto.publicId()))
                .thenReturn(Mono.just(savedLanguage));
        var result = languageService.findLanguageByPublicId(languageDto.publicId());
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.getData()).isNotNull();
                })
                .verifyComplete();
    }

    LanguageRequestDto buildLanguageRequestDto() {
        return LanguageRequestDto.builder()
                .name("soti")
                .createdBy("SYSTEM")
                .build();
    }

    Language saveLanguage(Language language) {
        language.setId(UUID.randomUUID());
        language.setCreatedDate(LocalDateTime.now());
        return language;
    }

    LanguageDto buildLanguageDto(Language language) {
        return new LanguageDto(language.getPublicId(), language.getName(), language.getStatus());
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
}