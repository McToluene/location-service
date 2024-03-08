package com.mctolueneam.locationservice.controllers;

import com.mctoluene.locationservice.controllers.LanguageController;
import com.mctoluene.locationservice.domains.dtos.LanguageDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.LanguageRequestDto;
import com.mctoluene.locationservice.models.Language;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.TracingService;
import com.mctoluene.locationservice.services.main.LanguageService;
import com.mctolueneam.commons.response.AppResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(LanguageController.class)
class LanguageControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private LanguageService languageService;

    @MockBean
    TracingService tracingService;

    @MockBean
    MessageSourceService messageSourceService;

    Language savedLanguage;

    Language language;

    LanguageDto languageDto;

    @BeforeEach
    void setUp() {

        language = buildLanguageEntity();
        savedLanguage = saveLanguageEntity(language);
        languageDto = buildLanguageDto(language);
    }

    @Test
    void createNewLanguage() {
        Mono<AppResponse> response = Mono.just(new AppResponse(HttpStatus.CREATED.value(),
                "language created successfully",
                "language created successfully", languageDto, null));
        var requestDto = buildLanguageRequestDto();
        when(languageService.createNewLanguage(requestDto)).thenReturn(response);
        webTestClient.post()
                .uri("/api/v1/location/languages").contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .header("x-trace-id", UUID.randomUUID().toString())
                .body(Mono.just(requestDto), LanguageRequestDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .json("{\"status\":201,\"message\":\"language created successfully\",\"supportDescriptiveMessage\":\"language created successfully\"}")
                .jsonPath("$.status").isEqualTo(201)
                .jsonPath("$.message").isEqualTo("language created successfully")
                .jsonPath("$.supportDescriptiveMessage").isEqualTo("language created successfully");
    }

    @Test
    void findLanguageByPublicId() {
        Mono<AppResponse> response = Mono.just(new AppResponse(HttpStatus.OK.value(),
                "language retrieved successfully",
                "language retrieved successfully", languageDto, null));

        when(languageService.findLanguageByPublicId(any())).thenReturn(response);
        String uri = "/api/v1/location/languages/" + languageDto.publicId();
        webTestClient
                .get()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("x-trace-id", UUID.randomUUID().toString())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json("{\"status\":200,\"message\":\"language retrieved successfully\",\"supportDescriptiveMessage\":\"language retrieved successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    void findAllLanguages() {
        List<Language> languages = new ArrayList<>();
        savedLanguage.setStatus(Status.ACTIVE.name());
        languages.add(savedLanguage);
        var languagePage = new PageImpl<>(languages);
        AppResponse appResponse = new AppResponse(HttpStatus.OK.value(),
                "language retrieved successfully",
                "language retrieved successfully", languagePage, null);

        when(languageService.findAllLanguages(1, 1))
                .thenReturn(Mono.just(appResponse));

        String uri = "/api/v1/location/languages?page=1&size=1";
        webTestClient
                .get()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("x-trace-id", UUID.randomUUID().toString())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json("{\"status\":200,\"message\":\"language retrieved successfully\",\"supportDescriptiveMessage\":\"language retrieved successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    Language buildLanguageEntity() {
        Language language = Language.builder()
                .name("Ilorin")
                .publicId(UUID.randomUUID())
                .build();

        language.setCreatedBy("SYSTEM");
        language.setCreatedDate(LocalDateTime.now());
        return language;
    }

    Language saveLanguageEntity(Language language) {
        language.setId(UUID.randomUUID());
        language.setCreatedDate(LocalDateTime.now());
        return language;
    }

    LanguageDto buildLanguageDto(Language language) {
        return new LanguageDto(language.getPublicId(), language.getName(), language.getStatus());
    }

    LanguageRequestDto buildLanguageRequestDto() {
        return LanguageRequestDto.builder()
                .name("Akani")
                .createdBy("SYSTEM")
                .build();
    }
}