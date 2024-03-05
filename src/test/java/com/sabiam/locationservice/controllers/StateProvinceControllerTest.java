package com.sabiam.locationservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mctoluene.locationservice.controllers.StateProvinceController;
import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.StateProvinceRequestDto;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.models.CountrySetting;
import com.mctoluene.locationservice.models.StateProvince;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.TracingService;
import com.mctoluene.locationservice.services.main.StateProvinceService;
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
@WebFluxTest(StateProvinceController.class)
class StateProvinceControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private StateProvinceService stateProvinceService;

    @MockBean
    TracingService tracingService;

    @MockBean
    MessageSourceService messageSourceService;

    StateProvince savedStateProvince;

    StateProvince stateProvince;

    Country country;

    StateProvinceDto stateProvinceDto;

    CountryDto countryDto;

    @BeforeEach
    void setUp() {
        stateProvince = buildStateEntity();
        savedStateProvince = saveStateProvinceEntity(stateProvince);
        country = buildCountryEntity();
        countryDto = buildCountryDto(country);
        stateProvinceDto = buildStateProvinceDto(stateProvince, countryDto);
    }

    @Test
    void createNewStateProvince() {
        Mono<AppResponse<StateProvinceDto>> response = Mono.just(new AppResponse<>(HttpStatus.CREATED.value(),
                "State province created successfully", "State province created successfully", stateProvinceDto, null));
        var requestDto = buildStateProvinceRequestDto();

        when(stateProvinceService.createNewStateProvince(requestDto)).thenReturn(response);
        webTestClient.post().uri("/api/v1/location/state-provinces")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .header("x-trace-id", UUID.randomUUID().toString())
                .body(Mono.just(requestDto), StateProvinceRequestDto.class).exchange().expectStatus().isCreated()
                .expectBody()
                .json("{\"status\":201,\"message\":\"State province created successfully\",\"supportDescriptiveMessage\":\"State province created successfully\"}")
                .jsonPath("$.status").isEqualTo(201).jsonPath("$.message")
                .isEqualTo("State province created successfully").jsonPath("$.supportDescriptiveMessage")
                .isEqualTo("State province created successfully");
    }

    @Test
    void findStateProvinceByPublicId() {

        Mono<AppResponse<StateProvinceDto>> response = Mono
                .just(new AppResponse<>(HttpStatus.OK.value(), "State province retrieved successfully",
                        "State province retrieved successfully", stateProvinceDto, null));

        when(stateProvinceService.findStateProvinceByPublicId(any())).thenReturn(response);
        String uri = "/api/v1/location/state-provinces/" + stateProvinceDto.publicId();
        webTestClient.get().uri(uri).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("x-trace-id", UUID.randomUUID().toString()).exchange().expectStatus().isOk().expectBody()
                .json("{\"status\":200,\"message\":\"State province retrieved successfully\",\"supportDescriptiveMessage\":\"State province retrieved successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    void deactivateStateProvince() {
        Mono<AppResponse<StateProvinceDto>> response = Mono
                .just(new AppResponse<>(HttpStatus.OK.value(), "state province deactivated successfully",
                        "state province deactivated successfully", stateProvinceDto, null));

        when(stateProvinceService.deactivateStateProvince(any())).thenReturn(response);
        String uri = "/api/v1/location/state-provinces/" + stateProvinceDto.publicId() + "/deactivate";
        webTestClient.patch().uri(uri).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("x-trace-id", UUID.randomUUID().toString()).exchange().expectStatus().isOk().expectBody()
                .json("{\"status\":200,\"message\":\"state province deactivated successfully\",\"supportDescriptiveMessage\":\"state province deactivated successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    void activateStateProvince() {
        Mono<AppResponse<StateProvinceDto>> response = Mono
                .just(new AppResponse<>(HttpStatus.OK.value(), "state province activated successfully",
                        "state province activated successfully", stateProvinceDto, null));

        when(stateProvinceService.activateStateProvince(any())).thenReturn(response);
        String uri = "/api/v1/location/state-provinces/" + stateProvinceDto.publicId() + "/activate";
        webTestClient.patch().uri(uri).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("x-trace-id", UUID.randomUUID().toString()).exchange().expectStatus().isOk().expectBody()
                .json("{\"status\":200,\"message\":\"state province activated successfully\",\"supportDescriptiveMessage\":\"state province activated successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    void findAllStateProvinces() {
        List<StateProvinceDto> stateProvinces = new ArrayList<>();
        savedStateProvince.setStatus(Status.ACTIVE.name());
        stateProvinces.add(stateProvinceDto);
        var stateProvincePage = new PageImpl<>(stateProvinces);
        AppResponse<PageImpl<StateProvinceDto>> appResponse = new AppResponse<>(HttpStatus.OK.value(),
                "State province retrieved successfully", "State province retrieved successfully", stateProvincePage,
                null);

        when(stateProvinceService.findAllStateProvinces(1, 1, Status.ACTIVE.name(), countryDto.publicId(), "", ""))
                .thenReturn(Mono.just(appResponse));

        String uri = "/api/v1/location/state-provinces?page=1&size=1&countryId=" + countryDto.publicId()
                + "&status=ACTIVE";
        webTestClient.get().uri(uri).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("x-trace-id", UUID.randomUUID().toString()).exchange().expectStatus().isOk().expectBody()
                .json("{\"status\":200,\"message\":\"State province retrieved successfully\",\"supportDescriptiveMessage\":\"State province retrieved successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    void findStateProvinceByPublicIds() {
        List<StateProvinceDto> stateProvinceDtos = new ArrayList<>();
        stateProvinceDtos.add(stateProvinceDto);
        Mono<AppResponse<List<StateProvinceDto>>> response = Mono
                .just(new AppResponse<>(HttpStatus.OK.value(), "State province retrieved successfully",
                        "State province retrieved successfully", stateProvinceDtos, null));
        List<UUID> request = new ArrayList<>();
        request.add(UUID.randomUUID());

        when(stateProvinceService.findStateProvinceByStatePublicIds(any())).thenReturn(response);
        String uri = "/api/v1/location/state-provinces/states/";
        webTestClient.post().uri(uri).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("x-trace-id", UUID.randomUUID().toString()).body(Mono.just(request), List.class).exchange()
                .expectStatus().isOk().expectBody()
                .json("{\"status\":200,\"message\":\"State province retrieved successfully\",\"supportDescriptiveMessage\":\"State province retrieved successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    StateProvince buildStateEntity() {
        StateProvince stateProvince = StateProvince.builder().capital("Ilorin").code("IL").countryId(UUID.randomUUID())
                .name("Ilorin").publicId(UUID.randomUUID()).build();

        stateProvince.setCreatedBy("SYSTEM");
        stateProvince.setCreatedDate(LocalDateTime.now());
        return stateProvince;
    }

    StateProvince saveStateProvinceEntity(StateProvince stateProvince) {
        stateProvince.setId(UUID.randomUUID());
        stateProvince.setCreatedDate(LocalDateTime.now());
        return stateProvince;
    }

    Country buildCountryEntity() {
        Country country = Country.builder().countryName("NIGERIA").dialingCode("234").publicId(UUID.randomUUID())
                .threeLetterCode("NGA").twoLetterCode("NG").build();

        country.setCreatedBy("SYSTEM");
        country.setCreatedDate(LocalDateTime.now());
        return country;
    }

    StateProvinceDto buildStateProvinceDto(StateProvince stateProvince, CountryDto countryDto) {
        return new StateProvinceDto(stateProvince.getPublicId(), stateProvince.getName(), stateProvince.getCapital(),
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

    StateProvinceRequestDto buildStateProvinceRequestDto() {
        return StateProvinceRequestDto.builder().capital("Ilorin").code("IL").countryId(UUID.randomUUID())
                .createdBy("SYSTEM").capital("Ilorin").name("Ilorin").build();
    }
}