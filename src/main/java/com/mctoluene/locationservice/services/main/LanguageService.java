package com.mctoluene.locationservice.services.main;

import com.mctoluene.locationservice.domains.requestdtos.LanguageRequestDto;
import com.mctolueneam.commons.response.AppResponse;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LanguageService {

    Mono<AppResponse> createNewLanguage(LanguageRequestDto languageRequestDto);

    Mono<AppResponse> findLanguageByPublicId(UUID publicId);

    Mono<AppResponse> findAllLanguages(int page, int size);

}
