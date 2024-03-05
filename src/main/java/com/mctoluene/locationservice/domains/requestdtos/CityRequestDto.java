package com.mctoluene.locationservice.domains.requestdtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
public class CityRequestDto {

    @NotEmpty(message = "Name is required")
    private String name;
    @NotEmpty(message = "Created by is required")
    private String createdBy;
    @NotNull(message = "State province id is required")
    private UUID stateProvinceId;
}
