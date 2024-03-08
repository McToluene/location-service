package com.mctoluene.locationservice.services.main.impl;

import com.mctoluene.locationservice.domains.dtos.LanguageDto;
import com.mctoluene.locationservice.domains.requestdtos.LanguageRequestDto;
import com.mctoluene.locationservice.exceptions.PageableException;
import com.mctoluene.locationservice.helpers.LanguageHelper;
import com.mctoluene.locationservice.models.Language;
import com.mctoluene.locationservice.services.internal.LanguageInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.main.LanguageService;
import com.mctolueneam.commons.exceptions.ConflictException;
import com.mctolueneam.commons.response.AppResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageInternalService languageInternalService;
    private final MessageSourceService messageSourceService;

    private static final String LANGUAGE_RETRIEVED = "language.retrieved.successfully";

    @Override
    public Mono<AppResponse> createNewLanguage(LanguageRequestDto languageRequestDto) {

        return languageInternalService.findLanguageByName(languageRequestDto.getName().toUpperCase())
                .flatMap(foundLanguage -> Mono.error(new ConflictException(messageSourceService
                        .getMessageByKey("language.name.already.exist.error"))))
                .switchIfEmpty(createLanguage(languageRequestDto))
                .map(languageDto -> new AppResponse(HttpStatus.CREATED.value(),
                        messageSourceService.getMessageByKey("language.created.successfully"),
                        messageSourceService.getMessageByKey("language.created.successfully"),
                        languageDto, null));
    }

    private Mono<LanguageDto> createLanguage(LanguageRequestDto languageRequestDto) {
        Language language = LanguageHelper.buildLanguage(languageRequestDto);
        return languageInternalService.saveLanguage(language).map(LanguageHelper::buildLanguageDto);
    }

    @Override
    public Mono<AppResponse> findLanguageByPublicId(UUID publicId) {
        return languageInternalService.findLanguageByPublicId(publicId)
                .map(language -> {
                    LanguageDto languageDto = LanguageHelper.buildLanguageDto(language);
                    return new AppResponse(HttpStatus.OK.value(),
                            messageSourceService.getMessageByKey(LANGUAGE_RETRIEVED),
                            messageSourceService.getMessageByKey(LANGUAGE_RETRIEVED),
                            languageDto, null);
                });
    }

    @Override
    public Mono<AppResponse> findAllLanguages(int page, int size) {
        log.info("about to get languages by page {} and size {} ", page, size);
        if (!validatePage(page, size))
            throw new PageableException("Invalid Page or Size number");
        PageRequest request = PageRequest.of(page - 1, size);
        Flux<Language> allLanguages = languageInternalService.findAllLanguages(request);
        return this.convertLanguageToPageableDto(allLanguages, request, languageInternalService.count())
                .map(result -> new AppResponse(HttpStatus.OK.value(),
                        messageSourceService.getMessageByKey(LANGUAGE_RETRIEVED),
                        messageSourceService.getMessageByKey(LANGUAGE_RETRIEVED),
                        result, null));
    }

    private Mono<PageImpl<LanguageDto>> convertLanguageToPageableDto(Flux<Language> languages, Pageable pageable,
            Mono<Long> languageCount) {
        return languages
                .map(LanguageHelper::buildLanguageDto)
                .collectList()
                .zipWith(languageCount)
                .map(t -> {
                    Pageable finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
                    return new PageImpl<>(t.getT1(), finalPageable, t.getT2());
                });
    }

    private static boolean validatePage(int page, int size) {
        return page >= 1 && size > 0;
    }

}
