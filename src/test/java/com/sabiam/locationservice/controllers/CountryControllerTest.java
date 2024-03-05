package com.sabiam.locationservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mctoluene.locationservice.controllers.CountryController;
import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.models.CountrySetting;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.TracingService;
import com.mctoluene.locationservice.services.main.CountryService;
import com.sabiam.commons.exceptions.UnProcessableEntityException;
import com.sabiam.commons.response.AppResponse;

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
@WebFluxTest(CountryController.class)
class CountryControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    CountryService countryService;

    @MockBean
    MessageSourceService messageSourceService;

    @MockBean
    TracingService tracingService;

    @Test
    void findCountryByPublicId() {
        Country country = buildCountryEntity();
        Country savedCountry = saveCountryEntity(country);
        CountryDto countryDto = buildCountryDto(savedCountry);

        Mono<AppResponse<CountryDto>> response = Mono.just(new AppResponse<>(HttpStatus.OK.value(),
                "country retrieved successfully",
                "country retrieved successfully", countryDto, null));

        when(countryService.findCountryByPublicId(any())).thenReturn(response);

        String uri = "/api/v1/location/countries/" + countryDto.publicId();

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
                .json("{\"status\":200,\"message\":\"country retrieved successfully\",\"supportDescriptiveMessage\":\"country retrieved successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    void activateCountry() {

        Country country = buildCountryEntity();
        Country savedCountry = saveCountryEntity(country);
        savedCountry.setStatus(Status.ACTIVE.name());
        CountryDto countryDto = buildCountryDto(savedCountry);

        Mono<AppResponse<CountryDto>> response = Mono.just(new AppResponse<>(HttpStatus.OK.value(),
                "country enabled successfully",
                "country enabled successfully", countryDto, null));

        when(countryService.activateCountry(any())).thenReturn(response);

        String uri = "/api/v1/location/countries/" + countryDto.publicId() + "/activate";

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
                .json("{\"status\":200,\"message\":\"country enabled successfully\",\"supportDescriptiveMessage\":\"country enabled successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    void deactivateCountry() {

        Country country = buildCountryEntity();
        Country savedCountry = saveCountryEntity(country);
        savedCountry.setStatus(Status.INACTIVE.name());
        CountryDto countryDto = buildCountryDto(savedCountry);

        Mono<AppResponse<CountryDto>> response = Mono.just(new AppResponse<>(HttpStatus.OK.value(),
                "country disabled successfully",
                "country disabled successfully", countryDto, null));

        when(countryService.deactivateCountry(any())).thenReturn(response);

        String uri = "/api/v1/location/countries/" + countryDto.publicId() + "/deactivate";

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
                .json("{\"status\":200,\"message\":\"country disabled successfully\",\"supportDescriptiveMessage\":\"country disabled successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
    }

    @Test
    void findAllCountries() {

        List<Country> countries = new ArrayList<>();

        Country country = buildCountryEntity();
        Country savedCountry = saveCountryEntity(country);
        savedCountry.setStatus(Status.ACTIVE.name());

        countries.add(savedCountry);

        var countryPage = new PageImpl<>(countries);

        AppResponse appResponse = new AppResponse(HttpStatus.OK.value(),
                "country retrieved successfully",
                "country retrieved successfully", countryPage, null);

        when(countryService.findAllCountries(1, 1, Status.ACTIVE.name(), "", "")).thenReturn(Mono.just(appResponse));

        String uri = "/api/v1/location/countries?page=1&size=1&status=ACTIVE";

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
                .json("{\"status\":200,\"message\":\"country retrieved successfully\",\"supportDescriptiveMessage\":\"country retrieved successfully\"}")
                .jsonPath("$.status").isEqualTo(200);
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

    Country saveCountryEntity(Country country) {
        country.setId(UUID.randomUUID());
        country.setCreatedDate(LocalDateTime.now());
        return country;
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
}