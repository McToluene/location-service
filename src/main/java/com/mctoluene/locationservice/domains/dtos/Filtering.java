package com.mctoluene.locationservice.domains.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@Builder
public class Filtering {
    private Pageable pageable;
    private String name;
    private String status;
    private String sortDirection;
}
