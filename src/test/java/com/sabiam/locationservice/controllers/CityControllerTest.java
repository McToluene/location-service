package com.sabiam.locationservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mctoluene.locationservice.controllers.CityController;
import com.mctoluene.locationservice.domains.dtos.CityDto;
import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.CityRequestDto;
import com.mctoluene.locationservice.models.City;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.models.CountrySetting;
import com.mctoluene.locationservice.models.StateProvince;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.TracingService;
import com.mctoluene.locationservice.services.main.CityService;
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
@WebFluxTest(CityController.class)
class CityControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CityService cityService;

    @MockBean
    TracingService tracingService;

    @MockBean
    MessageSourceService messageSourceService;

    StateProvinceDto stateProvinceDto;

    StateProvince stateProvince;

    City savedCity;

    City city;

    CityDto cityDto;

    @BeforeEach
    void setUp() {

        city = buildCityEntity();
        savedCity = saveCityEntity(city);
        stateProvince = buildStateEntity();
        Country country = buildCountryEntity();
        CountryDto countryDto = buildCountryDto(country);
        stateProvinceDto = buildStateProvinceDto(stateProvince, countryDto);
        cityDto = buildCityDto(city, stateProvinceDto);
    }

    @Test
    void createNewCity() {
        Mono<AppResponse<CityDto>> response = Mono.just(new AppResponse<>(HttpStatus.CREATED.value(),
                "city created successfully",
                "city created successfully", cityDto, null));
        var requestDto = buildCityRequestDto();
        when(cityService.createNewCity(requestDto)).thenReturn(response);
        webTestClient.post()
                .uri("/api/v1/location/cities").contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .header("x-trace-id", UUID.randomUUID().toString())
                .body(Mono.just(requestDto), CityRequestDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .json("{\"status\":201,\"message\":\"city created successfully\",\"supportDescriptiveMessage\":\"city created successfully\"}")
                .jsonPath("$.status").isEqualTo(201)
                .jsonPath("$.message").isEqualTo("city created successfully")
                .jsonPath("$.supportDescriptiveMessage").isEqualTo("city created successfully");
    }

    @Test
    void findCityByPublicId() {
        Mono<AppResponse<CityDto>> response = Mono.just(new AppResponse<>(HttpStatus.OK.value(),
                "city retrieved successfully",
                "city retrieved successfully", cityDto, null));

        when(cityService.findCityByPublicId(any())).thenReturn(response);
        String uri = "/api/v1/location/cities/" + cityDto.publicId();
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
                .json("{\"status\":200,\"message\":\"city retrieved successfully\",\"supportDescriptiveMessage\":\"city retrieved successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    void deactivateCity() {
        Mono<AppResponse<CityDto>> response = Mono.just(new AppResponse<>(HttpStatus.OK.value(),
                "city deactivated successfully",
                "city deactivated successfully", cityDto, null));

        when(cityService.deactivateCity(any())).thenReturn(response);
        String uri = "/api/v1/location/cities/" + stateProvinceDto.publicId() + "/deactivate";
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
                .json("{\"status\":200,\"message\":\"city deactivated successfully\",\"supportDescriptiveMessage\":\"city deactivated successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    void activateCity() {
        Mono<AppResponse<CityDto>> response = Mono.just(new AppResponse<>(HttpStatus.OK.value(),
                "city activated successfully",
                "city activated successfully", cityDto, null));

        when(cityService.activateCity(any())).thenReturn(response);
        String uri = "/api/v1/location/cities/" + stateProvinceDto.publicId() + "/activate";
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
                .json("{\"status\":200,\"message\":\"city activated successfully\",\"supportDescriptiveMessage\":\"city activated successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    void findAllCities() {
        List<City> cities = new ArrayList<>();
        savedCity.setStatus(Status.ACTIVE.name());
        cities.add(savedCity);
        var cityPage = new PageImpl<>(cities);
        AppResponse appResponse = new AppResponse(HttpStatus.OK.value(),
                "city retrieved successfully",
                "city retrieved successfully", cityPage, null);

        when(cityService.findAllCities(1, 1, Status.ACTIVE.name(), stateProvinceDto.publicId(), "", ""))
                .thenReturn(Mono.just(appResponse));

        String uri = "/api/v1/location/cities?page=1&size=1&stateProvinceId=" + stateProvinceDto.publicId()
                + "&status=ACTIVE";
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
                .json("{\"status\":200,\"message\":\"city retrieved successfully\",\"supportDescriptiveMessage\":\"city retrieved successfully\"}")
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

    City saveCityEntity(City city) {
        city.setId(UUID.randomUUID());
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

    CityRequestDto buildCityRequestDto() {
        return CityRequestDto.builder()
                .stateProvinceId(UUID.randomUUID())
                .name("Ilorin")
                .createdBy("SYSTEM")
                .build();
    }
}