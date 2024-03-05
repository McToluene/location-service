package com.sabiam.locationservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mctoluene.locationservice.controllers.LocalGovernmentController;
import com.mctoluene.locationservice.domains.dtos.CityDto;
import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.domains.dtos.LocalGovernmentDto;
import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.LocalGovernmentRequestDto;
import com.mctoluene.locationservice.models.*;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.TracingService;
import com.mctoluene.locationservice.services.main.LocalGovernmentService;
import com.sabiam.commons.exceptions.UnProcessableEntityException;
import com.sabiam.commons.response.AppResponse;

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

import static com.mctoluene.locationservice.helpers.LocationHelper.getEnabledDisabled;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(LocalGovernmentController.class)
class LocalGovernmentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private LocalGovernmentService localGovernmentService;

    @MockBean
    MessageSourceService messageSourceService;

    @MockBean
    TracingService tracingService;

    City city;

    CityDto cityDto;

    LocalGovernment savedLocalGovernment;

    LocalGovernment localGovernment;

    LocalGovernmentDto localGovernmentDto;

    @BeforeEach
    void setUp() {
        localGovernment = buildLocalGovernmentEntity();
        savedLocalGovernment = saveLocalGovernmentEntity(localGovernment);
        city = buildCityEntity();
        Country country = buildCountryEntity();
        CountryDto countryDto = buildCountryDto(country);
        StateProvince stateProvince = buildStateEntity();
        StateProvinceDto stateProvinceDto = buildStateProvinceDto(stateProvince, countryDto);
        cityDto = buildCityDto(city, stateProvinceDto);
        localGovernmentDto = buildLocalGovernmentDto(localGovernment, cityDto);
    }

    @Test
    void createNewLocalGovernment() {
        Mono<AppResponse<LocalGovernmentDto>> response = Mono.just(new AppResponse<>(HttpStatus.CREATED.value(),
                "local government retrieved successfully",
                "local government retrieved successfully", localGovernmentDto, null));
        var requestDto = buildLocalGovernmentRequestDto();
        when(localGovernmentService.createNewLocalGovernment(requestDto)).thenReturn(response);
        webTestClient.post()
                .uri("/api/v1/location/local-government")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .header("x-trace-id", UUID.randomUUID().toString())
                .body(Mono.just(requestDto), LocalGovernmentRequestDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .json("{\"status\":201,\"message\":\"local government retrieved successfully\",\"supportDescriptiveMessage\":\"local government retrieved successfully\"}")
                .jsonPath("$.status").isEqualTo(201)
                .jsonPath("$.message").isEqualTo("local government retrieved successfully")
                .jsonPath("$.supportDescriptiveMessage").isEqualTo("local government retrieved successfully");
    }

    @Test
    void findLocalGovernmentByPublicId() {
        Mono<AppResponse<LocalGovernmentDto>> response = Mono.just(new AppResponse<>(HttpStatus.OK.value(),
                "local government retrieved successfully",
                "local government retrieved successfully", localGovernmentDto, null));

        when(localGovernmentService.findLocalGovernmentByPublicId(any())).thenReturn(response);
        String uri = "/api/v1/location/local-government/" + localGovernmentDto.publicId();
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
                .json("{\"status\":200,\"message\":\"local government retrieved successfully\",\"supportDescriptiveMessage\":\"local government retrieved successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    void deactivateLocalGovernment() {
        Mono<AppResponse<LocalGovernmentDto>> response = Mono.just(new AppResponse<>(HttpStatus.OK.value(),
                "local government deactivated successfully",
                "local government deactivated successfully", localGovernmentDto, null));

        when(localGovernmentService.deactivateLocalGovernment(any())).thenReturn(response);
        String uri = "/api/v1/location/local-government/" + localGovernmentDto.publicId() + "/deactivate";
        webTestClient
                .patch()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("x-trace-id", UUID.randomUUID().toString())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json("{\"status\":200,\"message\":\"local government deactivated successfully\",\"supportDescriptiveMessage\":\"local government deactivated successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    void activateLocalGovernment() {
        Mono<AppResponse<LocalGovernmentDto>> response = Mono.just(new AppResponse<>(HttpStatus.OK.value(),
                "local government activated successfully",
                "local government activated successfully", localGovernmentDto, null));

        when(localGovernmentService.activateLocalGovernment(any())).thenReturn(response);
        String uri = "/api/v1/location/local-government/" + localGovernmentDto.publicId() + "/activate";
        webTestClient
                .patch()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("x-trace-id", UUID.randomUUID().toString())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json("{\"status\":200,\"message\":\"local government activated successfully\",\"supportDescriptiveMessage\":\"local government activated successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    void findAllLocalGovernments() {
        List<LocalGovernmentDto> localGovernments = new ArrayList<>();
        localGovernments.add(localGovernmentDto);
        var pageLocalGovernment = new PageImpl<>(localGovernments);
        AppResponse<PageImpl<LocalGovernmentDto>> appResponse = new AppResponse<>(HttpStatus.OK.value(),
                "local government retrieved successfully",
                "local government retrieved successfully", pageLocalGovernment, null);

        when(localGovernmentService.findAllLocalGovernment(1, 1, Status.ACTIVE.name(), cityDto.publicId(), "", ""))
                .thenReturn(Mono.just(appResponse));

        String uri = "/api/v1/location/local-government?page=1&size=1&cityId=" + cityDto.publicId() + "&status=ACTIVE";
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
                .json("{\"status\":200,\"message\":\"local government retrieved successfully\",\"supportDescriptiveMessage\":\"local government retrieved successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    City buildCityEntity() {
        City city = City.builder()
                .name("Ilorin")
                .publicId(UUID.randomUUID())
                .stateProvinceId(UUID.randomUUID())
                .build();

        city.setCreatedBy("SYSTEM");
        city.setCreatedDate(LocalDateTime.now());
        return city;
    }

    StateProvince buildStateEntity() {
        StateProvince stateProvince = StateProvince.builder()
                .capital("Ilorin")
                .code("IL")
                .countryId(UUID.randomUUID())
                .name("Ilorin")
                .publicId(UUID.randomUUID())
                .build();

        stateProvince.setCreatedBy("SYSTEM");
        stateProvince.setCreatedDate(LocalDateTime.now());
        return stateProvince;
    }

    Country buildCountryEntity() {
        Country country = Country.builder()
                .countryName("NIGERIA")
                .dialingCode("234")
                .publicId(UUID.randomUUID())
                .threeLetterCode("NGA")
                .twoLetterCode("NG")
                .build();

        country.setCreatedBy("SYSTEM");
        country.setCreatedDate(LocalDateTime.now());
        return country;
    }

    StateProvinceDto buildStateProvinceDto(StateProvince stateProvince, CountryDto countryDto) {
        return new StateProvinceDto(stateProvince.getPublicId(),
                stateProvince.getName(), stateProvince.getCapital(),
                stateProvince.getCode(), stateProvince.getStatus(), countryDto);

    }

    CountryDto buildCountryDto(Country country) {
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

    CityDto buildCityDto(City city, StateProvinceDto stateProvinceDto) {
        return new CityDto(city.getPublicId(), city.getName(), city.getStatus(), stateProvinceDto);
    }

    LocalGovernmentDto buildLocalGovernmentDto(LocalGovernment localGovernment, CityDto cityDto) {
        return new LocalGovernmentDto(localGovernment.getPublicId(), localGovernment.getName(),
                localGovernment.getStatus(), cityDto);
    }

    LocalGovernment saveLocalGovernmentEntity(LocalGovernment localGovernment) {
        localGovernment.setId(UUID.randomUUID());
        localGovernment.setCreatedDate(LocalDateTime.now());
        return localGovernment;
    }

    LocalGovernment buildLocalGovernmentEntity() {
        LocalGovernment localGovernment = LocalGovernment.builder()
                .name("Ilorin-West")
                .publicId(UUID.randomUUID())
                .cityId(UUID.randomUUID())
                .build();

        localGovernment.setCreatedBy("SYSTEM");
        localGovernment.setCreatedDate(LocalDateTime.now());
        return localGovernment;
    }

    LocalGovernmentRequestDto buildLocalGovernmentRequestDto() {
        return LocalGovernmentRequestDto.builder()
                .cityId(UUID.randomUUID())
                .createdBy("Ojulari")
                .name("Ilorin-West")
                .build();
    }
}