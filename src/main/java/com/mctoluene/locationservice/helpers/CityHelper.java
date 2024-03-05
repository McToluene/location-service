package com.mctoluene.locationservice.helpers;

import com.mctoluene.locationservice.domains.dtos.CityDto;
import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.CityRequestDto;
import com.mctoluene.locationservice.models.City;
import com.mctoluene.locationservice.models.StateProvince;
import com.sabiam.commons.response.AppResponse;

import org.springframework.http.ResponseEntity;

import static com.mctoluene.locationservice.helpers.LocationHelper.getEnabledDisabled;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

public class CityHelper {
    private CityHelper() {
    }

    public static CityDto buildCityDto(City city, StateProvince stateProvince) {
        StateProvinceDto stateProvinceDto = StateProvinceHelper.buildStateProvinceDto(stateProvince);
        return new CityDto(city.getPublicId(), city.getName(), getEnabledDisabled(city.getStatus()), stateProvinceDto);
    }

    public static City buildCity(CityRequestDto cityRequestDto, StateProvince stateProvince) {

        City city = City.builder()
                .name(cityRequestDto.getName().toUpperCase())
                .stateProvinceId(stateProvince.getId())
                .publicId(UUID.randomUUID())
                .build();

        city.setCreatedBy(cityRequestDto.getCreatedBy());
        city.setCreatedDate(LocalDateTime.now());
        city.setStatus(Status.INACTIVE.name());

        return city;
    }

    public static CityDto buildCityDto(City city) {
        return new CityDto(city.getPublicId(), city.getName(), getEnabledDisabled(city.getStatus()), null);
    }

    public static ResponseEntity<AppResponse<CityDto>> buildCityResponse(AppResponse<CityDto> response) {
        CityDto cityDto = response.getData();
        return ResponseEntity.created(URI
                .create(String.format("/api/v1/location/cities/%s", cityDto.publicId()))).body(response);
    }
}
