package com.mctolueneam.locationservice.helpers;

import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.StateProvinceRequestDto;
import com.mctoluene.locationservice.helpers.StateProvinceHelper;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.models.StateProvince;
import com.mctolueneam.commons.response.AppResponse;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class StateProvinceHelperTest {

    @Test
    void buildStateProvinceDto() {

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

        var stateProvince = StateProvince.builder()
                .publicId(UUID.randomUUID())
                .countryId(country.getId())
                .name("Lagos")
                .capital("Ikeja")
                .code("LG")
                .build();

        stateProvince.setStatus(Status.ACTIVE.name());
        stateProvince.setCreatedDate(LocalDateTime.now());
        stateProvince.setCreatedBy("a@a.com");

        var stateProvinceDto = StateProvinceHelper.buildStateProvinceDto(stateProvince, country);

        assertThat(stateProvinceDto).isNotNull();
        assertThat(stateProvinceDto.countryDto()).isNotNull();
        assertThat(stateProvinceDto.countryDto().publicId()).isEqualTo(country.getPublicId());
        assertThat(stateProvinceDto.name()).isNotBlank();
    }

    @Test
    void testBuildStateProvinceDto() {
        var stateProvince = StateProvince.builder()
                .publicId(UUID.randomUUID())
                .countryId(UUID.randomUUID())
                .name("Lagos")
                .capital("Ikeja")
                .code("LG")
                .build();
        stateProvince.setStatus(Status.ACTIVE.name());
        stateProvince.setCreatedDate(LocalDateTime.now());
        stateProvince.setCreatedBy("a@a.com");

        var stateProvinceDto = StateProvinceHelper.buildStateProvinceDto(stateProvince);

        assertThat(stateProvinceDto).isNotNull();
        assertThat(stateProvinceDto.capital()).isNotBlank();
        assertThat(stateProvinceDto.name()).isEqualTo(stateProvince.getName());
    }

    @Test
    void buildStateProvinceResponse() {

        var countryDto = new CountryDto(UUID.randomUUID(), "NIGERIA", "NG",
                "NGA", "234", "a@a.com", Status.ACTIVE.name(), null);

        var stateProvinceDto = new StateProvinceDto(UUID.randomUUID(), "Lagos", "Ikeja",
                "LG", Status.ACTIVE.name(), countryDto);

        AppResponse appResponse = new AppResponse(HttpStatus.CREATED.value(), "success",
                "", stateProvinceDto, null);

        var response = StateProvinceHelper.buildStateProvinceResponse(appResponse);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void buildStateProvince() {
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

        StateProvinceRequestDto stateProvinceRequestDto = StateProvinceRequestDto.builder()
                .capital("IKEJA")
                .code("LG")
                .countryId(UUID.randomUUID())
                .name("Lagos")
                .createdBy("a@a.com")
                .build();

        var stateProvince = StateProvinceHelper.buildStateProvince(stateProvinceRequestDto, country);

        assertThat(stateProvince).isNotNull();
        assertThat(stateProvince.getCountryId()).isEqualTo(country.getId());
        assertThat(stateProvince.getName()).isUpperCase();
        assertThat(stateProvince.getCapital()).isNotBlank();
    }
}