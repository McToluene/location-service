package com.mctoluene.locationservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CountrySetting {
    private List<CountryMetaData> languages;
    private List<CountryMetaData> currencies;
}
