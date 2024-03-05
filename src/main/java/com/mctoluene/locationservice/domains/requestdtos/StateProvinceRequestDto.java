package com.mctoluene.locationservice.domains.requestdtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StateProvinceRequestDto implements Serializable {

    @NotEmpty(message = "Name is required")
    private String name;
    @NotEmpty(message = "Code is required")
    @Size(min = 2, max = 5, message = "Code must be between 2 and 5 characters")
    private String code;
    @NotEmpty(message = "Capital is required")
    private String capital;
    @NotEmpty(message = "Created by is required")
    private String createdBy;
    @NotNull(message = "Country id is required")
    private UUID countryId;
}
