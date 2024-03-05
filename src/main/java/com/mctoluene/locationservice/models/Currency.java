package com.mctoluene.locationservice.models;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("currencies")
@EqualsAndHashCode(callSuper = false)
public class Currency extends AbstractBaseEntity {
    private UUID publicId;
    private String name;
    private String code;
}
