package com.mctoluene.locationservice.helpers;

import com.mctoluene.locationservice.domains.dtos.CityDto;
import com.mctoluene.locationservice.domains.dtos.LocalGovernmentDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.LocalGovernmentRequestDto;
import com.mctoluene.locationservice.models.City;
import com.mctoluene.locationservice.models.LocalGovernment;
import com.mctoluene.locationservice.models.StateProvince;
import com.sabiam.commons.response.AppResponse;

import org.springframework.http.ResponseEntity;

import static com.mctoluene.locationservice.helpers.LocationHelper.getEnabledDisabled;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

public class LocalGovernmentHelper {
    private LocalGovernmentHelper() {
    }

    public static LocalGovernmentDto buildLocalGovernmentDto(LocalGovernment localGovernment, City city) {
        CityDto cityDto = CityHelper.buildCityDto(city);
        return new LocalGovernmentDto(localGovernment.getPublicId(), localGovernment.getName(),
                getEnabledDisabled(localGovernment.getStatus()), cityDto);

    }

    public static LocalGovernmentDto buildLocalGovernmentDto(LocalGovernment localGovernment, City city,
            StateProvince stateProvince) {
        CityDto cityDto = CityHelper.buildCityDto(city, stateProvince);
        return new LocalGovernmentDto(localGovernment.getPublicId(), localGovernment.getName(),
                getEnabledDisabled(localGovernment.getStatus()), cityDto);

    }

    public static LocalGovernment buildLocalGovernment(LocalGovernmentRequestDto localGovernmentRequestDto, City city) {

        LocalGovernment localGovernment = LocalGovernment.builder()
                .cityId(city.getId())
                .name(localGovernmentRequestDto.getName().toUpperCase())
                .publicId(UUID.randomUUID())
                .build();

        localGovernment.setStatus(Status.INACTIVE.name());
        localGovernment.setCreatedBy(localGovernmentRequestDto.getCreatedBy());
        localGovernment.setCreatedDate(LocalDateTime.now());

        return localGovernment;
    }

    public static ResponseEntity<AppResponse<LocalGovernmentDto>> buildLocalGovernmentResponse(
            AppResponse<LocalGovernmentDto> response) {
        LocalGovernmentDto localGovernmentDto = (LocalGovernmentDto) response.getData();
        return ResponseEntity.created(URI
                .create(String.format("/api/v1/location/countries/%s", localGovernmentDto.publicId()))).body(response);
    }
}
