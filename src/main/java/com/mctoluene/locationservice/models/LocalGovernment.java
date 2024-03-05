package com.mctoluene.locationservice.models;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("lga_county")
@EqualsAndHashCode(callSuper = false)
public class LocalGovernment extends AbstractBaseEntity {

    private UUID publicId;
    private UUID cityId;
    private String name;
}
