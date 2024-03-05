package com.mctoluene.locationservice.helpers;

import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.domains.dtos.StateProvinceDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.StateProvinceRequestDto;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.models.StateProvince;
import com.sabiam.commons.response.AppResponse;

import org.springframework.http.ResponseEntity;

import static com.mctoluene.locationservice.helpers.LocationHelper.getEnabledDisabled;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

public class StateProvinceHelper {

    private StateProvinceHelper() {
    }

    public static StateProvinceDto buildStateProvinceDto(StateProvince stateProvince, Country country) {

        CountryDto countryDto = CountryHelper.buildCountryDto(country);
        return new StateProvinceDto(stateProvince.getPublicId(),
                stateProvince.getName(), stateProvince.getCapital(),
                stateProvince.getCode(), getEnabledDisabled(stateProvince.getStatus()), countryDto);

    }

    public static StateProvinceDto buildStateProvinceDto(StateProvince stateProvince) {

        return new StateProvinceDto(stateProvince.getPublicId(),
                stateProvince.getName(), stateProvince.getCapital(),
                stateProvince.getCode(), getEnabledDisabled(stateProvince.getStatus()), null);

    }

    public static ResponseEntity<AppResponse<StateProvinceDto>> buildStateProvinceResponse(
            AppResponse<StateProvinceDto> response) {
        StateProvinceDto stateProvinceDto = response.getData();
        return ResponseEntity.created(URI
                .create(String.format("/api/v1/location/countries/%s", stateProvinceDto.publicId()))).body(response);
    }

    public static StateProvince buildStateProvince(StateProvinceRequestDto stateProvinceRequestDto, Country country) {

        StateProvince stateProvince = StateProvince.builder()
                .capital(stateProvinceRequestDto.getCapital())
                .code(stateProvinceRequestDto.getCode())
                .name(stateProvinceRequestDto.getName().toUpperCase())
                .countryId(country.getId())
                .publicId(UUID.randomUUID())
                .build();

        stateProvince.setCreatedBy(stateProvinceRequestDto.getCreatedBy());
        stateProvince.setCreatedDate(LocalDateTime.now());
        stateProvince.setStatus(Status.INACTIVE.name());

        return stateProvince;
    }
}
