package com.mctolueneam.locationservice.services.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mctoluene.locationservice.domains.dtos.CityDto;
import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.domains.dtos.LocalGovernmentDto;
import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.LocalGovernmentRequestDto;
import com.mctoluene.locationservice.models.*;
import com.mctoluene.locationservice.services.internal.CityInternalService;
import com.mctoluene.locationservice.services.internal.LocalGovernmentInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.StateProvinceInternalService;
import com.mctoluene.locationservice.services.main.LocalGovernmentService;
import com.mctoluene.locationservice.services.main.impl.LocalGovernmentServiceImpl;
import com.mctolueneam.commons.exceptions.UnProcessableEntityException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.mctoluene.locationservice.helpers.LocationHelper.getEnabledDisabled;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class LocalGovernmentServiceTest {
    @Mock
    StateProvinceInternalService stateProvinceInternalService;

    @Mock
    CityInternalService cityInternalService;

    @Mock
    LocalGovernmentInternalService localGovernmentInternalService;

    @Mock
    MessageSourceService messageSourceService;

    LocalGovernmentService localGovernmentService;

    LocalGovernmentRequestDto localGovernmentRequestDto;

    LocalGovernment localGovernment;

    LocalGovernment savedLocalGovernment;

    City city;

    StateProvince stateProvince;

    AutoCloseable autoCloseable;

    LocalGovernmentDto localGovernmentDto;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        localGovernmentService = new LocalGovernmentServiceImpl(stateProvinceInternalService, cityInternalService,
                localGovernmentInternalService, messageSourceService);
        localGovernmentRequestDto = buildLocalGovernmentRequestDto();
        localGovernment = buildLocalGovernmentEntity();
        savedLocalGovernment = saveLocalGovernmentEntity(localGovernment);
        city = buildCityEntity();
        Country country = buildCountryEntity();
        CountryDto countryDto = buildCountryDto(country);
        stateProvince = buildStateEntity();
        StateProvinceDto stateProvinceDto = buildStateProvinceDto(stateProvince, countryDto);
        CityDto cityDto = buildCityDto(city, stateProvinceDto);
        localGovernmentDto = buildLocalGovernmentDto(localGovernment, cityDto);
    }

    @Test
    void createNewLocalGovernment() {
        when(cityInternalService.findCityByPublicId(any())).thenReturn(Mono.just(city));
        when(localGovernmentInternalService.findLocalGovernmentByName(anyString())).thenReturn(Mono.empty());
        when(localGovernmentInternalService.saveLocalGovernment(any())).thenReturn(Mono.just(savedLocalGovernment));

        var result = localGovernmentService.createNewLocalGovernment(localGovernmentRequestDto);
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
                })
                .verifyComplete();
    }

    @Test
    void findLocalGovernmentByPublicId() {
        when(cityInternalService.findById(any())).thenReturn(Mono.just(city));
        when(localGovernmentInternalService.findLocalGovernmentByPublicId(any()))
                .thenReturn(Mono.just(savedLocalGovernment));
        when(stateProvinceInternalService.findById(any())).thenReturn(Mono.just(stateProvince));
        var result = localGovernmentService.findLocalGovernmentByPublicId(localGovernmentDto.publicId());
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.getData()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void deactivateLocalGovernment() {
        when(localGovernmentInternalService.findLocalGovernmentByPublicId(any()))
                .thenReturn(Mono.just(savedLocalGovernment));
        savedLocalGovernment.setStatus(Status.INACTIVE.name());
        when(localGovernmentInternalService.saveLocalGovernment(any())).thenReturn(Mono.just(savedLocalGovernment));
        when(cityInternalService.findById(any())).thenReturn(Mono.just(city));
        var result = localGovernmentService.deactivateLocalGovernment(localGovernmentDto.publicId());
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.getData()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void activateLocalGovernment() {
        when(localGovernmentInternalService.findLocalGovernmentByPublicId(any()))
                .thenReturn(Mono.just(savedLocalGovernment));
        savedLocalGovernment.setStatus(Status.ACTIVE.name());
        when(localGovernmentInternalService.saveLocalGovernment(any())).thenReturn(Mono.just(savedLocalGovernment));
        when(cityInternalService.findById(any())).thenReturn(Mono.just(city));
        var result = localGovernmentService.activateLocalGovernment(localGovernmentDto.publicId());
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.getData()).isNotNull();
                })
                .verifyComplete();
    }

    // @Test
    // void findAllLocalGovernment() {
    // String status = Status.ACTIVE.name();
    // PageRequest request = PageRequest.of(0, 20);
    // var localGovernmentFlux = Flux.from(Mono.just(localGovernment));
    //
    // List<LocalGovernmentDto> localGovernmentDtos = new ArrayList<>();
    // localGovernmentDtos.add(LocalGovernmentHelper.buildLocalGovernmentDto(localGovernment,
    // city));
    // Mono<Long> localGovernmentCount = Mono.just(Long.valueOf(1));
    //
    // when(localGovernmentInternalService.countByStatusAndCityId(any(),
    // any())).thenReturn(localGovernmentCount);
    // when(cityInternalService.findCityByPublicId(any())).thenReturn(Mono.just(city));
    // when(localGovernmentInternalService.convertLocalGovernmentToPageableDto(localGovernmentFlux,
    // request, city, localGovernmentCount)).thenReturn(Mono.just(new
    // PageImpl<>(localGovernmentDtos)));
    //
    // var result = localGovernmentService.findAllLocalGovernment(1, 20, status,
    // UUID.randomUUID(), "", "");
    // StepVerifier.create(result)
    // .consumeNextWith(response -> {
    // assertThat(response).isNotNull();
    // assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    // assertThat(response.getData()).isNotNull();
    // })
    // .verifyComplete();
    // }

    @Test
    // void findAllLocalGovernment_empty_status() {
    // PageRequest request = PageRequest.of(0, 20);
    // var localGovernmentFlux = Flux.from(Mono.just(localGovernment));
    // List<LocalGovernmentDto> localGovernmentDtos = new ArrayList<>();
    // localGovernmentDtos.add(LocalGovernmentHelper.buildLocalGovernmentDto(localGovernment,
    // city));
    // Mono<Long> localGovernmentCount = Mono.just(Long.valueOf(1));
    // when(localGovernmentInternalService.countByStatusAndCityId(eq(null),
    // any())).thenReturn(localGovernmentCount);
    // when(cityInternalService.findCityByPublicId(any())).thenReturn(Mono.just(city));
    // when(localGovernmentInternalService.findLocalGovernmentByCity(request,
    // city)).thenReturn(localGovernmentFlux);
    // when(localGovernmentInternalService.convertLocalGovernmentToPageableDto(localGovernmentFlux,
    // request, city, localGovernmentCount)).thenReturn(Mono.just(new
    // PageImpl<>(localGovernmentDtos)));
    //
    // var result = localGovernmentService.findAllLocalGovernment(1, 20, null,
    // UUID.randomUUID(), "", "");
    // StepVerifier.create(result)
    // .consumeNextWith(response -> {
    // assertThat(response).isNotNull();
    // assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    // assertThat(response.getData()).isNotNull();
    // })
    // .verifyComplete();
    // }

    LocalGovernmentRequestDto buildLocalGovernmentRequestDto() {
        return LocalGovernmentRequestDto.builder()
                .cityId(UUID.randomUUID())
                .createdBy("Ojulari")
                .name("Ilorin-West")
                .build();
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

    LocalGovernment saveLocalGovernmentEntity(LocalGovernment localGovernment) {
        localGovernment.setId(UUID.randomUUID());
        localGovernment.setCreatedDate(LocalDateTime.now());
        return localGovernment;
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

    StateProvinceDto buildStateProvinceDto(StateProvince stateProvince, CountryDto countryDto) {
        return new StateProvinceDto(stateProvince.getPublicId(),
                stateProvince.getName(), stateProvince.getCapital(),
                stateProvince.getCode(), stateProvince.getStatus(), countryDto);

    }

    CityDto buildCityDto(City city, StateProvinceDto stateProvinceDto) {
        return new CityDto(city.getPublicId(), city.getName(), city.getStatus(), stateProvinceDto);
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

    LocalGovernmentDto buildLocalGovernmentDto(LocalGovernment localGovernment, CityDto cityDto) {
        return new LocalGovernmentDto(localGovernment.getPublicId(), localGovernment.getName(),
                localGovernment.getStatus(), cityDto);
    }
}