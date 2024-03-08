package com.mctolueneam.locationservice.helpers;

import com.mctoluene.locationservice.domains.dtos.CityDto;
import com.mctoluene.locationservice.domains.dtos.LocalGovernmentDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.LocalGovernmentRequestDto;
import com.mctoluene.locationservice.helpers.LocalGovernmentHelper;
import com.mctoluene.locationservice.models.City;
import com.mctoluene.locationservice.models.LocalGovernment;
import com.mctolueneam.commons.response.AppResponse;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LocalGovernmentHelperTest {

    @Test
    void buildLocalGovernmentDto() {

        City city = City.builder()
                .name("Lekki")
                .publicId(UUID.randomUUID())
                .stateProvinceId(UUID.randomUUID())
                .build();

        city.setStatus(Status.ACTIVE.name());
        city.setCreatedDate(LocalDateTime.now());
        city.setCreatedBy("a@a.com");
        city.setId(UUID.randomUUID());

        LocalGovernment localGovernment = LocalGovernment.builder()
                .publicId(UUID.randomUUID())
                .cityId(city.getId())
                .name("ETI-OSA")
                .build();

        localGovernment.setCreatedDate(LocalDateTime.now());
        localGovernment.setCreatedBy("a@a.com");
        localGovernment.setId(UUID.randomUUID());
        localGovernment.setStatus(Status.ACTIVE.name());

        var localGovernmentDto = LocalGovernmentHelper.buildLocalGovernmentDto(localGovernment, city);

        assertThat(localGovernmentDto).isNotNull();
        assertThat(localGovernmentDto.cityDto()).isNotNull();
        assertThat(localGovernmentDto.publicId()).isEqualTo(localGovernment.getPublicId());

    }

    @Test
    void buildLocalGovernment() {
        var localGovernmentRequestDto = LocalGovernmentRequestDto.builder()
                .createdBy("a@a.com")
                .cityId(UUID.randomUUID())
                .name("ETI-OSA")
                .build();

        City city = City.builder()
                .name("Lekki")
                .publicId(localGovernmentRequestDto.getCityId())
                .stateProvinceId(UUID.randomUUID())
                .build();

        city.setStatus(Status.ACTIVE.name());
        city.setCreatedDate(LocalDateTime.now());
        city.setCreatedBy("a@a.com");
        city.setId(UUID.randomUUID());

        var localGovernment = LocalGovernmentHelper.buildLocalGovernment(localGovernmentRequestDto, city);
        assertThat(localGovernment).isNotNull();
        assertThat(localGovernment.getCityId()).isNotNull();
    }

    @Test
    void buildLocalGovernmentResponse() {
        var cityDto = new CityDto(UUID.randomUUID(), "ETI-OSA", Status.ACTIVE.name(), null);
        var localGovernmentDto = new LocalGovernmentDto(UUID.randomUUID(), "ETI_OSA", Status.ACTIVE.name(), cityDto);

        AppResponse appResponse = new AppResponse(HttpStatus.CREATED.value(), "success",
                "", localGovernmentDto, null);

        var response = LocalGovernmentHelper.buildLocalGovernmentResponse(appResponse);

        AssertionsForClassTypes.assertThat(response).isNotNull();
        AssertionsForClassTypes.assertThat(response.getStatusCode().value()).isEqualTo(201);
    }
}