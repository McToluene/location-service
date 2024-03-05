package com.sabiam.locationservice.services.internal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mctoluene.locationservice.domains.dtos.LocalGovernmentDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.helpers.LocalGovernmentHelper;
import com.mctoluene.locationservice.models.City;
import com.mctoluene.locationservice.models.LocalGovernment;
import com.mctoluene.locationservice.repositories.LocalGovernmentRepository;
import com.mctoluene.locationservice.services.internal.LocalGovernmentInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.impl.LocalGovernmentInternalServiceImpl;

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

class LocalGovernmentInternalServiceTest {

    @Mock
    LocalGovernmentRepository localGovernmentRepository;

    @Mock
    MessageSourceService messageSourceService;

    LocalGovernmentInternalService localGovernmentInternalService;

    City city;

    LocalGovernment localGovernment;

    LocalGovernment savedLocalGovernment;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        localGovernmentInternalService = new LocalGovernmentInternalServiceImpl(localGovernmentRepository,
                messageSourceService);
        city = buildCityEntity();
        localGovernment = buildLocalGovernmentEntity();
        savedLocalGovernment = saveLocalGovernmentEntity(localGovernment);
    }

    @Test
    void saveLocalGovernment() {
        when(localGovernmentRepository.save(any())).thenReturn(Mono.just(savedLocalGovernment));
        var savedLocalGovernment = localGovernmentInternalService.saveLocalGovernment(localGovernment);

        StepVerifier.create(savedLocalGovernment)
                .consumeNextWith(localGovernment -> {
                    assertThat(localGovernment).isNotNull();
                    assertThat(localGovernment.getName()).isNotBlank();
                    assertThat(localGovernment.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findLocalGovernmentByPublicId() {
        when(localGovernmentRepository.findByPublicId(any())).thenReturn(Mono.just(localGovernment));
        var found = localGovernmentInternalService.findLocalGovernmentByPublicId(localGovernment.getPublicId());

        StepVerifier.create(found)
                .consumeNextWith(localGovernment -> {
                    assertThat(localGovernment).isNotNull();
                    assertThat(localGovernment.getName()).isNotBlank();
                    assertThat(localGovernment.getId()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findLocalGovernmentByName() {
        when(localGovernmentRepository.findLocalGovernmentByName(any())).thenReturn(Mono.just(savedLocalGovernment));
        var found = localGovernmentInternalService.findLocalGovernmentByName(localGovernment.getName());

        StepVerifier.create(found)
                .consumeNextWith(localGovernment -> {
                    assertThat(localGovernment).isNotNull();
                    assertThat(localGovernment.getName()).isNotBlank();
                    assertThat(localGovernment.getId()).isNotNull();
                })
                .verifyComplete();
    }

    // @Test
    // void findLocalGovernmentByCityAndStatus() {
    // Pageable pageable = PageRequest.of(0, 1);
    // when(localGovernmentRepository.findAllByCityIdAndStatus(city.getPublicId(),
    // Status.ACTIVE.name(), pageable)).thenReturn(Flux.just(savedLocalGovernment));
    // when(localGovernmentInternalService.findLocalGovernmentByCityAndStatus(pageable,
    // city, Status.ACTIVE.name())).thenReturn(Flux.just(savedLocalGovernment));
    //
    // var allCities =
    // localGovernmentInternalService.findLocalGovernmentByCityAndStatus(pageable,
    // city, Status.ACTIVE.name());
    // StepVerifier.create(allCities)
    // .expectNext(savedLocalGovernment)
    // .expectComplete()
    // .verify();
    // }

    @Test
    void findLocalGovernmentByCity() {
        Pageable pageable = PageRequest.of(0, 1);
        given(localGovernmentRepository.findAllByCityId(city.getPublicId(), pageable))
                .willReturn(Flux.just(savedLocalGovernment));
        given(localGovernmentInternalService.findLocalGovernmentByCity(pageable, city))
                .willReturn(Flux.just(savedLocalGovernment));

        var allLocalGovernments = localGovernmentInternalService.findLocalGovernmentByCity(pageable, city);
        StepVerifier.create(allLocalGovernments)
                .expectNext(savedLocalGovernment)
                .expectComplete()
                .verify();
    }

    @Test
    void convertLocalGovernmentToPageable() {
        Pageable pageable = PageRequest.of(0, 1);
        List<LocalGovernmentDto> localGovernmentDtos = new ArrayList<>();
        localGovernmentDtos.add(LocalGovernmentHelper.buildLocalGovernmentDto(localGovernment, city));

        Mono<Long> localGovermentCount = Mono.just(Long.valueOf(1));

        var allCities = localGovernmentInternalService.convertLocalGovernmentToPageableDto(
                Flux.just(savedLocalGovernment), pageable, city, localGovermentCount);
        StepVerifier.create(allCities)
                .expectAccessibleContext()
                .then()
                .expectNext(new PageImpl<>(localGovernmentDtos, pageable, 1))
                .expectComplete()
                .verify();
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
}