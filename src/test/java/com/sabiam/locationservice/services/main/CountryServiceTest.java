package com.sabiam.locationservice.services.main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.services.internal.CountryInternalService;
import com.mctoluene.locationservice.services.internal.CurrencyInternalService;
import com.mctoluene.locationservice.services.internal.LanguageInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.main.CountryService;
import com.mctoluene.locationservice.services.main.impl.CountryServiceImpl;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CountryServiceTest {

    @Mock
    CountryInternalService countryInternalService;

    @Mock
    MessageSourceService messageSourceService;

    @Mock
    LanguageInternalService languageInternalService;

    @Mock
    CurrencyInternalService currencyInternalService;

    CountryService countryService;

    AutoCloseable autoCloseable;

    Country country;

    Country savedCountry;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        countryService = new CountryServiceImpl(countryInternalService, languageInternalService,
                currencyInternalService, messageSourceService);
        country = buildCountryEntity();
        savedCountry = saveCountryEntity(country);
    }

    @Test
    void findCountryByPublicId() {
        when(countryInternalService.findCountryByPublicId(any())).thenReturn(Mono.just(savedCountry));

        var result = countryService.findCountryByPublicId(country.getPublicId());

        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                })
                .verifyComplete();
    }

    @Test
    void findCountryByDialingCode() {
        when(countryInternalService.findCountryByDialingCode(any())).thenReturn(Mono.just(savedCountry));

        var result = countryService.findCountryByDialingCode(country.getDialingCode());

        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                })
                .verifyComplete();
    }

    @Test
    void deactivateCountry() {
        when(countryInternalService.findCountryByPublicId(any())).thenReturn(Mono.just(savedCountry));

        savedCountry.setStatus(Status.INACTIVE.name());

        when(countryInternalService.saveCountry(any())).thenReturn(Mono.just(savedCountry));

        var result = countryService.deactivateCountry(country.getPublicId());

        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                })
                .verifyComplete();
    }

    @Test
    void activateCountry() {
        when(countryInternalService.findCountryByPublicId(any())).thenReturn(Mono.just(savedCountry));

        savedCountry.setStatus(Status.ACTIVE.name());

        when(countryInternalService.saveCountry(any())).thenReturn(Mono.just(savedCountry));

        var result = countryService.activateCountry(country.getPublicId());

        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                })
                .verifyComplete();
    }

    // @Test
    // void findAllCountries() {
    // String status = Status.ACTIVE.name();
    // PageRequest request = PageRequest.of(0, 20);
    // var countriesFlux = Flux.from(Mono.just(country));
    // List<CountryDto> countryDtos = new ArrayList<>();
    // countryDtos.add(CountryHelper.buildCountryDto(country));
    // Mono<Long> countryCount = Mono.just(Long.valueOf(1));
    // when(countryInternalService.countByStatus(status)).thenReturn(countryCount);
    // when(countryInternalService.convertCountryToPageable(countriesFlux, request,
    // countryCount)).thenReturn(Mono.just(new PageImpl<>(countryDtos)));
    // when(countryInternalService.findAllCountry(request)).thenReturn(countriesFlux);
    //
    // var result = countryService.findAllCountries(1, 20, status, "", "");
    // StepVerifier.create(result)
    // .consumeNextWith(response -> {
    // assertThat(response).isNotNull();
    // assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    // assertThat(response.getData()).isNotNull();
    // })
    // .verifyComplete();
    // }

    // @Test
    // void findAllCountries_empty_status() {
    //
    // PageRequest request = PageRequest.of(0, 20);
    // var countriesFlux = Flux.from(Mono.just(country));
    // List<CountryDto> countryDtos = new ArrayList<>();
    // countryDtos.add(CountryHelper.buildCountryDto(country));
    // Mono<Long> countryCount = Mono.just(Long.valueOf(1));
    // when(countryInternalService.countByStatus(null)).thenReturn(countryCount);
    // when(countryInternalService.findAllCountry(request)).thenReturn(countriesFlux);
    // when(countryInternalService.convertCountryToPageable(countriesFlux, request,
    // countryCount)).thenReturn(Mono.just(new PageImpl<>(countryDtos)));
    // when(countryInternalService.findAllCountry(request)).thenReturn(countriesFlux);
    //
    // var result = countryService.findAllCountries(1, 20, null, "", "");
    // StepVerifier.create(result)
    // .consumeNextWith(response -> {
    // assertThat(response).isNotNull();
    // assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    // assertThat(response.getData()).isNotNull();
    // })
    // .verifyComplete();
    // }

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

    @AfterEach
    void tearDown() {
    }
}