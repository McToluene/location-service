package com.sabiam.locationservice.helpers;

import com.mctoluene.locationservice.domains.dtos.CityDto;
import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.CityRequestDto;
import com.mctoluene.locationservice.helpers.CityHelper;
import com.mctoluene.locationservice.models.City;
import com.mctoluene.locationservice.models.StateProvince;
import com.sabiam.commons.response.AppResponse;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CityHelperTest {

    @Test
    void buildCityDto() {

        var stateProvince = StateProvince.builder()
                .publicId(UUID.randomUUID())
                .countryId(UUID.randomUUID())
                .name("Lagos")
                .capital("Ikeja")
                .code("LG")
                .build();
        stateProvince.setStatus(Status.ACTIVE.name());
        stateProvince.setCreatedDate(LocalDateTime.now());
        stateProvince.setId(UUID.randomUUID());
        stateProvince.setCreatedBy("a@a.com");

        City city = City.builder()
                .name("Lekki")
                .publicId(UUID.randomUUID())
                .stateProvinceId(stateProvince.getId())
                .build();

        city.setStatus(Status.ACTIVE.name());
        city.setCreatedDate(LocalDateTime.now());
        city.setCreatedBy("a@a.com");
        city.setId(UUID.randomUUID());

        var cityDto = CityHelper.buildCityDto(city);

        assertThat(cityDto).isNotNull();
        assertThat(cityDto.stateProvinceDto()).isNull();
        assertThat(cityDto.status()).isNotBlank();
    }

    @Test
    void buildCity() {
        CityRequestDto cityRequestDto = CityRequestDto.builder()
                .createdBy("a@a.com")
                .name("Ikeja")
                .stateProvinceId(UUID.randomUUID())
                .build();

        var stateProvince = StateProvince.builder()
                .publicId(UUID.randomUUID())
                .countryId(UUID.randomUUID())
                .name("Lagos")
                .capital("Ikeja")
                .code("LG")
                .build();
        stateProvince.setStatus(Status.ACTIVE.name());
        stateProvince.setCreatedDate(LocalDateTime.now());
        stateProvince.setId(UUID.randomUUID());
        stateProvince.setCreatedBy("a@a.com");

        var city = CityHelper.buildCity(cityRequestDto, stateProvince);

        assertThat(city.getPublicId()).isNotNull();
        assertThat(city.getStateProvinceId()).isEqualTo(stateProvince.getId());

    }

    @Test
    void testBuildCityDto() {

        var stateProvince = StateProvince.builder()
                .publicId(UUID.randomUUID())
                .countryId(UUID.randomUUID())
                .name("Lagos")
                .capital("Ikeja")
                .code("LG")
                .build();
        stateProvince.setStatus(Status.ACTIVE.name());
        stateProvince.setCreatedDate(LocalDateTime.now());
        stateProvince.setId(UUID.randomUUID());
        stateProvince.setCreatedBy("a@a.com");

        City city = City.builder()
                .name("Lekki")
                .publicId(UUID.randomUUID())
                .stateProvinceId(stateProvince.getId())
                .build();

        city.setStatus(Status.ACTIVE.name());
        city.setCreatedDate(LocalDateTime.now());
        city.setCreatedBy("a@a.com");
        city.setId(UUID.randomUUID());

        var cityDto = CityHelper.buildCityDto(city, stateProvince);

        assertThat(cityDto).isNotNull();
        assertThat(cityDto.stateProvinceDto()).isNotNull();
        assertThat(cityDto.status()).isNotBlank();

    }

    @Test
    void buildCityResponse() {

        var stateProvinceDto = new StateProvinceDto(UUID.randomUUID(), "lagos", "Ikeja",
                "LG", Status.ACTIVE.name(), null);

        var cityDto = new CityDto(UUID.randomUUID(), "ETI-OSA", Status.ACTIVE.name(), stateProvinceDto);

        AppResponse appResponse = new AppResponse(HttpStatus.CREATED.value(), "success",
                "", cityDto, null);

        var response = CityHelper.buildCityResponse(appResponse);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(201);
    }
}