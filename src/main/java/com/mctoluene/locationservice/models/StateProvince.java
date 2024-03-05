package com.mctoluene.locationservice.models;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("states_provinces")
@EqualsAndHashCode(callSuper = false)
public class StateProvince extends AbstractBaseEntity {

    private UUID publicId;
    private UUID countryId;
    private String name;
    private String code;
    private String capital;
}
