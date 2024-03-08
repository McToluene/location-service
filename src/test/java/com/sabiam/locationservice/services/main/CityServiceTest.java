package com.mctolueneam.locationservice.services.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mctoluene.locationservice.domains.dtos.CityDto;
import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.CityRequestDto;
import com.mctoluene.locationservice.models.City;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.models.CountrySetting;
import com.mctoluene.locationservice.models.StateProvince;
import com.mctoluene.locationservice.services.internal.CityInternalService;
import com.mctoluene.locationservice.services.internal.CountryInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.StateProvinceInternalService;
import com.mctoluene.locationservice.services.main.CityService;
import com.mctoluene.locationservice.services.main.impl.CityServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CityServiceTest {

    @Mock
    CountryInternalService countryInternalService;
    @Mock
    StateProvinceInternalService stateProvinceInternalService;

    @Mock
    CityInternalService cityInternalService;

    @Mock
    MessageSourceService messageSourceService;

    CityService cityService;

    CityRequestDto cityRequestDto;

    City city;

    City savedCity;

    StateProvince stateProvince;

    AutoCloseable autoCloseable;

    CityDto cityDto;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        cityService = new CityServiceImpl(countryInternalService, cityInternalService, stateProvinceInternalService,
                messageSourceService);
        cityRequestDto = buildCityRequestDto();
        city = buildCityEntity();
        savedCity = saveCity(city);
        stateProvince = buildStateEntity();
        Country country = buildCountryEntity();
        CountryDto countryDto = buildCountryDto(country);
        StateProvinceDto stateProvinceDto = buildStateProvinceDto(stateProvince, countryDto);
        cityDto = buildCityDto(city, stateProvinceDto);
    }

    @Test
    void createNewCity() {
        when(stateProvinceInternalService.findStateProvinceByPublicId(any())).thenReturn(Mono.just(stateProvince));
        when(cityInternalService.findCityByName(anyString())).thenReturn(Mono.empty());
        when(cityInternalService.saveCity(any())).thenReturn(Mono.just(savedCity));

        var result = cityService.createNewCity(cityRequestDto);
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
                })
                .verifyComplete();

    }

    @Test
    void findCityByPublicId() {
        when(stateProvinceInternalService.findById(any())).thenReturn(Mono.just(stateProvince));
        when(cityInternalService.findCityByPublicId(any())).thenReturn(Mono.just(savedCity));
        var result = cityService.findCityByPublicId(cityDto.publicId());
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.getData()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void deactivateCity() {
        when(cityInternalService.findCityByPublicId(any())).thenReturn(Mono.just(savedCity));
        savedCity.setStatus(Status.INACTIVE.name());
        when(cityInternalService.saveCity(any())).thenReturn(Mono.just(savedCity));
        when(stateProvinceInternalService.findById(any())).thenReturn(Mono.just(stateProvince));
        var result = cityService.deactivateCity(stateProvince.getPublicId());
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.getData()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void activateCity() {
        when(cityInternalService.findCityByPublicId(any())).thenReturn(Mono.just(savedCity));
        savedCity.setStatus(Status.ACTIVE.name());
        when(cityInternalService.saveCity(any())).thenReturn(Mono.just(savedCity));
        when(stateProvinceInternalService.findById(any())).thenReturn(Mono.just(stateProvince));
        var result = cityService.activateCity(stateProvince.getPublicId());
        StepVerifier.create(result)
                .consumeNextWith(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.getData()).isNotNull();
                })
                .verifyComplete();
    }

    // @Test
    // void findAllCities() {
    // String status = Status.ACTIVE.name();
    // PageRequest request = PageRequest.of(1, 20);
    // var cityFlux = Flux.from(Mono.just(city));
    // List<City> cities = new ArrayList<>();
    // cities.add(city);
    //
    // when(stateProvinceInternalService.findStateProvinceByPublicId(any())).thenReturn(Mono.just(stateProvince));
    // when(cityInternalService.findCityByStateProvinceAndStatus(request,
    // stateProvince, status)).thenReturn(cityFlux);
    // when(cityInternalService.convertCityToPageableDto(cityFlux,
    // request)).thenReturn(Mono.just(new PageImpl<>(cities)));
    // var result = cityService.findAllCities(1, 20, status, UUID.randomUUID());
    // StepVerifier.create(result)
    // .consumeNextWith(response -> {
    // assertThat(response).isNotNull();
    // assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    // assertThat(response.getData()).isNotNull();
    // })
    // .verifyComplete();
    // }

    CityRequestDto buildCityRequestDto() {
        return CityRequestDto.builder()
                .name("Ilorin")
                .stateProvinceId(UUID.randomUUID())
                .createdBy("SYSTEM")
                .build();
    }

    City saveCity(City city) {
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

    CityDto buildCityDto(City city, StateProvinceDto stateProvinceDto) {
        return new CityDto(city.getPublicId(), city.getName(), city.getStatus(), stateProvinceDto);
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
}