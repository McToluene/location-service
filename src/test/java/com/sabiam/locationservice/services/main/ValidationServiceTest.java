package com.sabiam.locationservice.services.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.models.LocalGovernment;
import com.mctoluene.locationservice.models.StateProvince;
import com.mctoluene.locationservice.services.internal.CountryInternalService;
import com.mctoluene.locationservice.services.internal.LocalGovernmentInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.StateProvinceInternalService;
import com.mctoluene.locationservice.services.main.ValidateCountryStateLgaService;
import com.mctoluene.locationservice.services.main.impl.ValidateCountryStateLgaServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ValidationServiceTest {

    @Mock
    CountryInternalService countryInternalService;
    @Mock
    StateProvinceInternalService stateProvinceInternalService;
    @Mock
    LocalGovernmentInternalService localGovernmentInternalService;
    @Mock
    MessageSourceService messageSourceService;
    ValidateCountryStateLgaService validationService;
    Country country;
    StateProvince stateProvince;
    LocalGovernment localGovernment;
    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        country = buildCountryEntity();
        validationService = new ValidateCountryStateLgaServiceImpl(countryInternalService, stateProvinceInternalService,
                localGovernmentInternalService, messageSourceService);
        stateProvince = buildStateEntity();
        localGovernment = buildLocalGovernmentEntity();
    }

    @Test
    void validateCountryStateLga() {

        when(countryInternalService.findCountryByPublicIdAndStatus(any(), any()))
                .thenReturn(Mono.just(country));
        when(stateProvinceInternalService.findStateProvinceByCountryAndStatus(any(), any()))
                .thenReturn(Flux.just(stateProvince));
        when(localGovernmentInternalService.findLocalGovernmentByPublicIdAndStatus(any(), any()))
                .thenReturn(Mono.just(localGovernment));

        var result = validationService.validateCountryStateLga(country.getPublicId(), stateProvince.getPublicId(),
                localGovernment.getPublicId());
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.getData()).isNotNull();
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
        country.setStatus("ACTIVE");
        return country;
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
        stateProvince.setStatus("ACTIVE");
        return stateProvince;
    }

    LocalGovernment buildLocalGovernmentEntity() {
        LocalGovernment localGovernment = LocalGovernment.builder()
                .name("Ilorin-West")
                .publicId(UUID.randomUUID())
                .cityId(UUID.randomUUID())
                .build();

        localGovernment.setCreatedBy("SYSTEM");
        localGovernment.setCreatedDate(LocalDateTime.now());
        localGovernment.setStatus("ACTIVE");
        return localGovernment;
    }
}
