package com.mctoluene.locationservice.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("languages")
@EqualsAndHashCode(callSuper = false)
public class Language {
    @Id
    private UUID id;
    private UUID publicId;
    private String name;
    private String createdBy;
    private LocalDateTime createdDate;
    private String status;
}
