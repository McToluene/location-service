package com.mctoluene.locationservice.domains.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record CurrencyDto(UUID publicId, String code, String name, LocalDateTime createdDate,
        LocalDateTime lastModifiedDate,
        String createdBy, String lastModifiedBy,
        String status) {

}
