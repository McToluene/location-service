package com.sabiam.locationservice.services.internal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mctoluene.locationservice.domains.dtos.CityDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.helpers.CityHelper;
import com.mctoluene.locationservice.models.City;
import com.mctoluene.locationservice.models.StateProvince;
import com.mctoluene.locationservice.repositories.CityRepository;
import com.mctoluene.locationservice.services.internal.CityInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.impl.CityInternalServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

class CityInternalServiceTest {

    @Mock
    CityRepository cityRepository;

    @Mock
    MessageSourceService messageSourceService;

    CityInternalService cityInternalService;

    StateProvince stateProvince;

    City city;

    City savedCity;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        cityInternalService = new CityInternalServiceImpl(cityRepository, messageSourceService);
        stateProvince = buildStateEntity();
        city = buildCityEntity();
        savedCity = saveCity(city);
    }

    @Test
    void saveCity() {
        when(cityRepository.save(any())).thenReturn(Mono.just(savedCity));
        var savedCity = cityInternalService.saveCity(city);

        StepVerifier.create(savedCity)
                .consumeNextWith(city -> {
                    assertThat(city).isNotNull();
                    assertThat(city.getName()).isNotBlank();
                    assertThat(city.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findCityByPublicId() {
        when(cityRepository.findByPublicId(any())).thenReturn(Mono.just(city));
        var foundCity = cityInternalService.findCityByPublicId(city.getPublicId());

        StepVerifier.create(foundCity)
                .consumeNextWith(city -> {
                    assertThat(city).isNotNull();
                    assertThat(city.getName()).isNotBlank();
                    assertThat(city.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findCityByName() {
        when(cityRepository.findCityByName(any())).thenReturn(Mono.just(savedCity));
        var foundState = cityInternalService.findCityByName(stateProvince.getName());

        StepVerifier.create(foundState)
                .consumeNextWith(stateProvince -> {
                    assertThat(stateProvince).isNotNull();
                    assertThat(stateProvince.getName()).isNotBlank();
                    assertThat(stateProvince.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findCityByStateProvince() {
        Pageable pageable = PageRequest.of(0, 1);
        given(cityRepository.findAllByStateProvinceId(stateProvince.getPublicId(), pageable))
                .willReturn(Flux.just(savedCity));
        given(cityInternalService.findCityByStateProvince(pageable, stateProvince)).willReturn(Flux.just(savedCity));

        var allCities = cityInternalService.findCityByStateProvince(pageable, stateProvince);
        StepVerifier.create(allCities)
                .expectNext(savedCity)
                .expectComplete()
                .verify();
    }

    @Test
    void convertCityToPageable() {
        Pageable pageable = PageRequest.of(0, 1);
        List<CityDto> cityDtoList = new ArrayList<>();
        cityDtoList.add(CityHelper.buildCityDto(city));
        Mono<Long> countCity = Mono.just(Long.valueOf(1));

        var allCities = cityInternalService.convertCityToPageableDto(Flux.just(savedCity), pageable, countCity);
        StepVerifier.create(allCities)
                .expectAccessibleContext()
                .then()
                .expectNext(new PageImpl<>(cityDtoList, pageable, 1))
                .expectComplete()
                .verify();
    }

    @Test
    void findById() {
        when(cityRepository.findById(city.getPublicId())).thenReturn(Mono.just(savedCity));
        var foundState = cityInternalService.findById(city.getPublicId());

        StepVerifier.create(foundState)
                .consumeNextWith(stateProvince -> {
                    assertThat(stateProvince).isNotNull();
                    assertThat(stateProvince.getName()).isNotBlank();
                    assertThat(stateProvince.getId()).isNotNull();
                })
                .verifyComplete();
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

    City saveCity(City city) {
        city.setId(UUID.randomUUID());
        city.setCreatedDate(LocalDateTime.now());
        return city;
    }
}