package com.sabiam.locationservice.services.internal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.repositories.CountryRepository;
import com.mctoluene.locationservice.services.internal.CountryInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.impl.CountryInternalServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CountryInternalServiceTest {

    @Mock
    CountryRepository countryRepository;

    @Mock
    MessageSourceService messageSourceService;

    CountryInternalService countryInternalService;

    AutoCloseable autoCloseable;

    Country country;

    Country savedCountry;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        countryInternalService = new CountryInternalServiceImpl(countryRepository, messageSourceService);
        country = buildCountryEntity();
        savedCountry = saveCountryEntity(country);
    }

    @Test
    void saveCountry() {

        when(countryRepository.save(any())).thenReturn(Mono.just(savedCountry));

        var saveCountry = countryInternalService.saveCountry(country);

        StepVerifier.create(saveCountry)
                .consumeNextWith(country1 -> {
                    assertThat(country1).isNotNull();
                    assertThat(country1.getCountryName()).isNotBlank();
                    assertThat(country1.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findCountryByPublicId() {
        when(countryRepository.findByPublicId(any())).thenReturn(Mono.just(country));
        var foundCountry = countryInternalService.findCountryByPublicId(country.getPublicId());

        StepVerifier.create(foundCountry)
                .consumeNextWith(country1 -> {
                    assertThat(country1).isNotNull();
                    assertThat(country1.getCountryName()).isNotBlank();
                    assertThat(country1.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findCountryByDialingCode() {
        when(countryRepository.findCountryByDialingCode(any())).thenReturn(Mono.just(country));
        var foundCountry = countryInternalService.findCountryByDialingCode(country.getDialingCode());

        StepVerifier.create(foundCountry)
                .consumeNextWith(country1 -> {
                    assertThat(country1).isNotNull();
                    assertThat(country1.getCountryName()).isNotBlank();
                    assertThat(country1.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findCountryByTwoLetterCode() {
        when(countryRepository.findCountryByTwoLetterCode(any())).thenReturn(Mono.just(country));
        var foundCountry = countryInternalService.findCountryByTwoLetterCode(country.getTwoLetterCode());

        StepVerifier.create(foundCountry)
                .consumeNextWith(country1 -> {
                    assertThat(country1).isNotNull();
                    assertThat(country1.getCountryName()).isNotBlank();
                    assertThat(country1.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findCountryByThreeLetterCode() {
        when(countryRepository.findCountryByThreeLetterCode(any())).thenReturn(Mono.just(country));
        var foundCountry = countryInternalService.findCountryByThreeLetterCode(country.getThreeLetterCode());

        StepVerifier.create(foundCountry)
                .consumeNextWith(country1 -> {
                    assertThat(country1).isNotNull();
                    assertThat(country1.getCountryName()).isNotBlank();
                    assertThat(country1.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findCountryByName() {
        when(countryRepository.findCountryByCountryName(anyString())).thenReturn(Mono.just(country));
        var foundCountry = countryInternalService.findCountryByName(country.getCountryName());

        StepVerifier.create(foundCountry)
                .consumeNextWith(country1 -> {
                    assertThat(country1).isNotNull();
                    assertThat(country1.getCountryName()).isNotBlank();
                    assertThat(country1.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findAllCountryByStatusAndName() {
        Pageable pageable = PageRequest.of(0, 1);
        when(countryRepository.findAllByStatusAndCountryNameContainingIgnoreCase(Status.ACTIVE.name(), "", pageable))
                .thenReturn(Flux.just(savedCountry));
        var allCountry = countryInternalService.findAllByStatusAndName(Status.ACTIVE.name(), "", pageable);

        StepVerifier.create(allCountry)
                .expectNext(savedCountry).expectComplete().verify();

    }

    @Test
    void findAllCountry() {
        Pageable pageable = PageRequest.of(0, 1);

        when(countryRepository.findAllBy(pageable)).thenReturn(Flux.just(savedCountry));

        var allCountry = countryInternalService.findAllCountry(pageable);

        StepVerifier.create(allCountry)
                .expectNext(savedCountry).expectComplete().verify();
    }

    @Test
    void findById() {
        when(countryRepository.findById(country.getPublicId())).thenReturn(Mono.just(savedCountry));
        var foundCountry = countryInternalService.findById(country.getPublicId());

        StepVerifier.create(foundCountry)
                .consumeNextWith(country -> {
                    assertThat(country).isNotNull();
                    assertThat(country.getCountryName()).isNotBlank();
                    assertThat(country.getId()).isNotNull();
                })
                .verifyComplete();
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

    @AfterEach
    public void cleanUpEach() {
        System.out.println("After Each cleanUpEach() method called");
    }

}