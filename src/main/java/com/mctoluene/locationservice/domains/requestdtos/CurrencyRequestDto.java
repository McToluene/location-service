package com.mctoluene.locationservice.domains.requestdtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRequestDto {

    @NotEmpty(message = "{currency.name.empty}")
    private String name;
    @NotEmpty(message = "{currency.code.empty}")
    private String code;
    @NotEmpty(message = "{currency.createdBy.empty}")
    private String createdBy;

}
