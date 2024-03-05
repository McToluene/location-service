package com.mctoluene.locationservice.models;

import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("countries")
@EqualsAndHashCode(callSuper = false)
public class Country extends AbstractBaseEntity {

    private UUID publicId;
    private String countryName;
    private String twoLetterCode;
    private String threeLetterCode;
    private String dialingCode;
    private String preferredStateName;
    private String preferredCityName;
    private String preferredLgaName;
    private Json countrySetting;
}
