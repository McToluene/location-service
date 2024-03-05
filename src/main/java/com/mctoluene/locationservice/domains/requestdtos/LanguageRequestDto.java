package com.mctoluene.locationservice.domains.requestdtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class LanguageRequestDto {

    @NotEmpty(message = "Name is required")
    private String name;
    @NotEmpty(message = "Created by is required")
    private String createdBy;

}
