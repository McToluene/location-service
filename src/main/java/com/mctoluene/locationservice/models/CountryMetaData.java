package com.mctoluene.locationservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CountryMetaData {
    private UUID id;
    private String name;
}
