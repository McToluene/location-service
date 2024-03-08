package com.mctolueneam.locationservice.services.internal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.helpers.StateProvinceHelper;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.models.StateProvince;
import com.mctoluene.locationservice.repositories.StateProvinceRepository;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.StateProvinceInternalService;
import com.mctoluene.locationservice.services.internal.impl.StateProvinceInternalServiceImpl;

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

class StateProvinceInternalServiceTest {

    @Mock
    StateProvinceRepository stateProvinceRepository;

    @Mock
    MessageSourceService messageSourceService;

    StateProvinceInternalService stateProvinceInternalService;

    Country country;

    StateProvince stateProvince;

    StateProvince savedStateProvince;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        stateProvinceInternalService = new StateProvinceInternalServiceImpl(stateProvinceRepository,
                messageSourceService);
        country = buildCountryEntity();
        stateProvince = buildStateEntity();
        savedStateProvince = saveStateProvinceEntity(stateProvince);
    }

    @Test
    void saveStateProvince() {
        when(stateProvinceRepository.save(any())).thenReturn(Mono.just(savedStateProvince));
        var saveStateProvince = stateProvinceInternalService.saveStateProvince(stateProvince);

        StepVerifier.create(saveStateProvince)
                .consumeNextWith(stateProvince -> {
                    assertThat(stateProvince).isNotNull();
                    assertThat(stateProvince.getName()).isNotBlank();
                    assertThat(stateProvince.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findStateProvinceByPublicId() {
        when(stateProvinceRepository.findByPublicId(any())).thenReturn(Mono.just(stateProvince));
        var foundState = stateProvinceInternalService.findStateProvinceByPublicId(stateProvince.getPublicId());

        StepVerifier.create(foundState)
                .consumeNextWith(stateProvince -> {
                    assertThat(stateProvince).isNotNull();
                    assertThat(stateProvince.getName()).isNotBlank();
                    assertThat(stateProvince.getId()).isNotNull();
                })
                .verifyComplete();

    }

    @Test
    void findStateProvinceByPublicIds() {
        when(stateProvinceRepository.findAllByPublicIdIn(any())).thenReturn(Flux.from(Mono.just(stateProvince)));
        var foundState = stateProvinceInternalService
                .findStateProvinceByPublicIds(List.of(stateProvince.getPublicId()));

        StepVerifier.create(foundState)
                .consumeNextWith(stateProvince -> {
                    assertThat(stateProvince).isNotNull();
                    assertThat(stateProvince.getName()).isNotBlank();
                    assertThat(stateProvince.getId()).isNotNull();
                })
                .verifyComplete();

    }

    @Test
    void findStateProvinceByName() {
        when(stateProvinceRepository.findStateProvinceByName(any())).thenReturn(Mono.just(savedStateProvince));
        var foundState = stateProvinceInternalService.findStateProvinceByName(stateProvince.getName());

        StepVerifier.create(foundState)
                .consumeNextWith(stateProvince -> {
                    assertThat(stateProvince).isNotNull();
                    assertThat(stateProvince.getName()).isNotBlank();
                    assertThat(stateProvince.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findStateProvinceByCountry() {
        Pageable pageable = PageRequest.of(0, 1);
        given(stateProvinceRepository.findAllBy(country.getPublicId(), pageable))
                .willReturn(Flux.just(savedStateProvince));
        given(stateProvinceInternalService.findStateProvinceByCountry(pageable, country))
                .willReturn(Flux.just(savedStateProvince));

        var allStateProvinces = stateProvinceInternalService.findStateProvinceByCountry(pageable, country);
        StepVerifier.create(allStateProvinces)
                .expectNext(savedStateProvince)
                .expectComplete()
                .verify();
    }

    @Test
    void convertStateProvinceToPageable() {
        Pageable pageable = PageRequest.of(0, 1);
        List<StateProvinceDto> stateProvinceDtoList = new ArrayList<>();
        stateProvinceDtoList.add(StateProvinceHelper.buildStateProvinceDto(stateProvince));

        Mono<Long> countState = Mono.just(Long.valueOf(1));

        var allStateProvinces = stateProvinceInternalService
                .convertStateProvinceToPageableDto(Flux.just(savedStateProvince), pageable, countState);
        StepVerifier.create(allStateProvinces)
                .expectAccessibleContext()
                .then()
                .expectNext(new PageImpl<>(stateProvinceDtoList, pageable, 1))
                .expectComplete()
                .verify();
    }

    @Test
    void findById() {
        when(stateProvinceRepository.findById(stateProvince.getPublicId())).thenReturn(Mono.just(savedStateProvince));
        var foundState = stateProvinceInternalService.findById(stateProvince.getPublicId());

        StepVerifier.create(foundState)
                .consumeNextWith(stateProvince -> {
                    assertThat(stateProvince).isNotNull();
                    assertThat(stateProvince.getName()).isNotBlank();
                    assertThat(stateProvince.getId()).isNotNull();
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
        country.setStatus(Status.ACTIVE.name());
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
        return stateProvince;
    }

    StateProvince saveStateProvinceEntity(StateProvince stateProvince) {
        stateProvince.setId(UUID.randomUUID());
        stateProvince.setCreatedDate(LocalDateTime.now());
        return stateProvince;
    }
}