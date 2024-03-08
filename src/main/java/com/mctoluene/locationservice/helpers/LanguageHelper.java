package com.mctoluene.locationservice.helpers;

import com.mctoluene.locationservice.domains.dtos.LanguageDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.LanguageRequestDto;
import com.mctoluene.locationservice.models.Language;
import com.mctolueneam.commons.response.AppResponse;

import org.springframework.http.ResponseEntity;

import static com.mctoluene.locationservice.helpers.LocationHelper.getEnabledDisabled;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

public class LanguageHelper {
    private LanguageHelper() {
    }

    public static Language buildLanguage(LanguageRequestDto languageRequestDto) {

        Language language = Language.builder()
                .name(languageRequestDto.getName().toUpperCase())
                .publicId(UUID.randomUUID())
                .build();

        language.setCreatedBy(languageRequestDto.getCreatedBy());
        language.setCreatedDate(LocalDateTime.now());
        language.setStatus(Status.INACTIVE.name());

        return language;
    }

    public static LanguageDto buildLanguageDto(Language language) {
        return new LanguageDto(language.getPublicId(), language.getName(), getEnabledDisabled(language.getStatus()));
    }

    public static ResponseEntity<AppResponse> buildLanguageResponse(AppResponse response) {
        LanguageDto languageDto = (LanguageDto) response.getData();
        return ResponseEntity.created(URI
                .create(String.format("/api/v1/location/languages/%s", languageDto.publicId()))).body(response);
    }
}
