package com.mctoluene.locationservice.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.models.CountryMetaData;
import com.mctoluene.locationservice.models.CountrySetting;
import com.sabiam.commons.exceptions.UnProcessableEntityException;
import com.sabiam.commons.response.AppResponse;

import org.springframework.http.ResponseEntity;

import static com.mctoluene.locationservice.helpers.LocationHelper.getEnabledDisabled;

import java.net.URI;
import java.util.List;
import java.util.UUID;

public class CountryHelper {
    private CountryHelper() {
    }

    public static CountryDto buildCountryDto(Country country) {
        try {
            CountrySetting countrySetting = null;
            if (country.getCountrySetting() != null)
                countrySetting = new ObjectMapper().readValue(country.getCountrySetting().asString(),
                        CountrySetting.class);

            return new CountryDto(country.getPublicId(), country.getCountryName(), country.getTwoLetterCode(),
                    country.getThreeLetterCode(), country.getDialingCode(), country.getCreatedBy(),
                    getEnabledDisabled(country.getStatus()), countrySetting);
        } catch (JsonProcessingException exception) {
            throw new UnProcessableEntityException("Failed to process country settings");
        }
    }

    public static ResponseEntity<AppResponse> buildCountryResponse(AppResponse response) {
        CountryDto countryDto = (CountryDto) response.getData();
        return ResponseEntity.created(URI
                .create(String.format("/api/v1/location/countries/%s", countryDto.publicId()))).body(response);
    }

    public static CountryMetaData buildCountryMeta(String name, UUID id) {
        CountryMetaData countryMetaData = new CountryMetaData();
        countryMetaData.setId(id);
        countryMetaData.setName(name);
        return countryMetaData;
    }

    public static String buildCountrySetting(List<CountryMetaData> currencies, List<CountryMetaData> languages) {
        try {
            CountrySetting setting = new CountrySetting();
            setting.setCurrencies(currencies);
            setting.setLanguages(languages);
            return new ObjectMapper().writeValueAsString(setting);
        } catch (JsonProcessingException exception) {
            throw new UnProcessableEntityException(exception.getMessage());
        }
    }
}
