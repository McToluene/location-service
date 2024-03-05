package com.mctoluene.locationservice.models;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("cities")
@EqualsAndHashCode(callSuper = false)
public class City extends AbstractBaseEntity {

    private UUID publicId;
    private UUID stateProvinceId;
    private String name;

}
