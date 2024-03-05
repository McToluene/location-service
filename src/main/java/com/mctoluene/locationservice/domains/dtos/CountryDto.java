package com.mctoluene.locationservice.domains.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mctoluene.locationservice.models.CountrySetting;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CountryDto(UUID publicId, String countryName, String twoLetterCode, String threeLetterCode,
        String dialingCode, String createdBy, String status, CountrySetting countrySetting) {
}
