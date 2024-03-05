package com.mctoluene.locationservice.domains.requestdtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalGovernmentRequestDto {

    @NotEmpty(message = "Name is required")
    private String name;
    @NotEmpty(message = "Created by is required")
    private String createdBy;
    @NotNull(message = "City id is required")
    private UUID cityId;
}
