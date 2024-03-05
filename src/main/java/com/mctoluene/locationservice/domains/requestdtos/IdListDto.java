package com.mctoluene.locationservice.domains.requestdtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class IdListDto {
    @NotNull(message = "{get.by.ids.null}")
    @Size(min = 1, message = "{get.by.ids.size}")
    private List<UUID> ids;
}
