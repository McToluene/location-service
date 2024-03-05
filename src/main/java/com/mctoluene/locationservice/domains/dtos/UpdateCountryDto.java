package com.mctoluene.locationservice.domains.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UpdateCountryDto {

    @NotNull
    @NotEmpty
    private List<UUID> languages;

    @NotNull
    @NotEmpty
    private List<UUID> currencies;
}
