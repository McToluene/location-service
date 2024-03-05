package com.sabiam.locationservice.helpers;

import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.domains.enums.EnumResponseAdapter;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.helpers.CountryHelper;
import com.mctoluene.locationservice.models.Country;
import com.sabiam.commons.response.AppResponse;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CountryHelperTest {

    @Test
    void buildCountryDto() {
        Country country = Country.builder()
                .countryName("NIGERIA")
                .dialingCode("234")
                .threeLetterCode("NGA")
                .twoLetterCode("NG")
                .publicId(UUID.randomUUID())
                .build();
        country.setStatus("INACTIVE");
        country.setCreatedBy("a@a.com");
        country.setCreatedDate(LocalDateTime.now());

        var countryDto = CountryHelper.buildCountryDto(country);

        assertThat(countryDto.countryName()).isNotBlank();
        assertThat(countryDto.status()).isEqualTo(EnumResponseAdapter.INACTIVE.toEnabledDisabled());
        assertThat(countryDto.publicId()).isNotNull();
        assertThat(countryDto.twoLetterCode()).isNotBlank();

    }

    @Test
    void buildCountryResponse() {
        var countryDto = new CountryDto(UUID.randomUUID(), "NIGERIA", "NG",
                "NGA", "234", "a@a.com", Status.ACTIVE.name(), null);

        AppResponse appResponse = new AppResponse(HttpStatus.CREATED.value(), "success",
                "", countryDto, null);

        var response = CountryHelper.buildCountryResponse(appResponse);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(201);
    }

}