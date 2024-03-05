package com.sabiam.locationservice.services.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.StateProvinceRequestDto;
import com.mctoluene.locationservice.helpers.StateProvinceHelper;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.models.CountrySetting;
import com.mctoluene.locationservice.models.StateProvince;
import com.mctoluene.locationservice.services.internal.CountryInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.StateProvinceInternalService;
import com.mctoluene.locationservice.services.main.StateProvinceService;
import com.mctoluene.locationservice.services.main.impl.StateProvinceServiceImpl;
import com.sabiam.commons.exceptions.UnProcessableEntityException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mctoluene.locationservice.helpers.LocationHelper.getEnabledDisabled;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class StateProvinceServiceTest {

    @Mock
    CountryInternalService countryInternalService;

    @Mock
    StateProvinceInternalService stateProvinceInternalService;

    @Mock
    MessageSourceService messageSourceService;

    StateProvinceService stateProvinceService;

    StateProvinceRequestDto stateProvinceRequestDto;

    StateProvince stateProvince;

    StateProvince savedStateProvince;

    Country country;

    AutoCloseable autoCloseable;

    StateProvinceDto stateProvinceDto;

    CountryDto countryDto;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        stateProvinceService = new StateProvinceServiceImpl(countryInternalService, stateProvinceInternalService,
                messageSourceService);
        stateProvinceRequestDto = buildStateProvinceRequestDto();
        stateProvince = buildStateEntity();
        savedStateProvince = saveStateProvinceEntity(stateProvince);
        country = buildCountryEntity();
        countryDto = buildCountryDto(country);
        stateProvinceDto = buildStateProvinceDto(stateProvince, countryDto);
    }

    @Test
    void createNewStateProvince() {
        when(countryInternalService.findCountryByPublicId(any())).thenReturn(Mono.just(country));
        when(stateProvinceInternalService.findStateProvinceByName(anyString())).thenReturn(Mono.empty());
        when(stateProvinceInternalService.saveStateProvince(any())).thenReturn(Mono.just(savedStateProvince));

        var result = stateProvinceService.createNewStateProvince(stateProvinceRequestDto);
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
                })
                .verifyComplete();
    }

    @Test
    void findStateProvinceByPublicId() {
        when(countryInternalService.findById(any())).thenReturn(Mono.just(country));
        when(stateProvinceInternalService.findStateProvinceByPublicId(any())).thenReturn(Mono.just(savedStateProvince));
        var result = stateProvinceService.findStateProvinceByPublicId(stateProvince.getPublicId());
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.getData()).isNotNull();
                    assertThat(response.getData()).isEqualTo(stateProvinceDto);
                })
                .verifyComplete();
    }

    @Test
    void deactivateStateProvince() {
        when(stateProvinceInternalService.findStateProvinceByPublicId(any())).thenReturn(Mono.just(savedStateProvince));
        savedStateProvince.setStatus(Status.INACTIVE.name());
        when(stateProvinceInternalService.saveStateProvince(any())).thenReturn(Mono.just(savedStateProvince));
        when(countryInternalService.findById(any())).thenReturn(Mono.just(country));
        var result = stateProvinceService.deactivateStateProvince(stateProvince.getPublicId());
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.getData()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void activateStateProvince() {
        when(stateProvinceInternalService.findStateProvinceByPublicId(any())).thenReturn(Mono.just(savedStateProvince));
        savedStateProvince.setStatus(Status.ACTIVE.name());
        when(stateProvinceInternalService.saveStateProvince(any())).thenReturn(Mono.just(savedStateProvince));
        when(countryInternalService.findById(any())).thenReturn(Mono.just(country));
        var result = stateProvinceService.deactivateStateProvince(stateProvince.getPublicId());
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.getData()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findAllStateProvinces() {
        PageRequest request = PageRequest.of(0, 20);
        var stateProvinces = Flux.from(Mono.just(stateProvince));
        List<StateProvinceDto> stateProvinceList = new ArrayList<>();
        stateProvinceList.add(StateProvinceHelper.buildStateProvinceDto(stateProvince));
        Mono<Long> stateProvinceCount = Mono.just(Long.valueOf(1));
        when(stateProvinceInternalService.countByStatusAndCountryId(any(), any())).thenReturn(stateProvinceCount);
        when(countryInternalService.findCountryByPublicId(any())).thenReturn(Mono.just(country));
        when(stateProvinceInternalService.convertStateProvinceToPageableDto(any(), any(), any()))
                .thenReturn(Mono.just(new PageImpl<>(stateProvinceList)));

        var result = stateProvinceService.findAllStateProvinces(1, 20, "ACTIVE", UUID.randomUUID(), "", "");
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.getData()).isNotNull();
                })
                .verifyComplete();

    }

    @Test
    void findStateProvinceByPublicIds() {
        when(stateProvinceInternalService.findStateProvinceByPublicIds(any()))
                .thenReturn(Flux.from(Mono.just(savedStateProvince)));
        var result = stateProvinceService.findStateProvinceByStatePublicIds(List.of(stateProvince.getPublicId()));
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.getData()).isNotNull();
                })
                .verifyComplete();
    }
    // @Test
    // void findAllStateProvinces_empty_status() {
    // PageRequest request = PageRequest.of(0, 20);
    // var stateProvinces = Flux.from(Mono.just(stateProvince));
    // List<StateProvinceDto> stateProvinceList = new ArrayList<>();
    // stateProvinceList.add(StateProvinceHelper.buildStateProvinceDto(stateProvince));
    // Mono<Long> stateProvinceCount = Mono.just(Long.valueOf(1));
    // when(stateProvinceInternalService.countByStatusAndCountryId(eq(null),
    // any())).thenReturn(stateProvinceCount);
    // when(countryInternalService.findCountryByPublicId(any())).thenReturn(Mono.just(country));
    // when(stateProvinceInternalService.findStateProvinceByCountry(request,
    // country)).thenReturn(stateProvinces);
    // when(stateProvinceInternalService.convertStateProvinceToPageableDto(stateProvinces,
    // request, stateProvinceCount)).thenReturn(Mono.just(new
    // PageImpl<>(stateProvinceList)));
    //
    // var result = stateProvinceService.findAllStateProvinces(1, 20, null,
    // UUID.randomUUID(), "", "");
    // StepVerifier.create(result)
    // .consumeNextWith(response -> {
    // assertThat(response).isNotNull();
    // assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    // assertThat(response.getData()).isNotNull();
    // })
    // .verifyComplete();
    // }

    StateProvinceRequestDto buildStateProvinceRequestDto() {
        return StateProvinceRequestDto.builder()
                .capital("Ilorin")
                .code("IL")
                .countryId(UUID.randomUUID())
                .createdBy("SYSTEM")
                .capital("Ilorin")
                .name("Ilorin")
                .build();
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

    StateProvince saveStateProvinceEntity(StateProvince stateProvince) {
        stateProvince.setId(UUID.randomUUID());
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
}