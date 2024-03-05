package com.mctoluene.locationservice.services.main.impl;

import com.mctoluene.locationservice.domains.dtos.CountryDto;
import com.mctoluene.locationservice.domains.dtos.UpdateCountryDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.helpers.CountryHelper;
import com.mctoluene.locationservice.helpers.LocationHelper;
import com.mctoluene.locationservice.models.Country;
import com.mctoluene.locationservice.models.CountryMetaData;
import com.mctoluene.locationservice.services.internal.CountryInternalService;
import com.mctoluene.locationservice.services.internal.CurrencyInternalService;
import com.mctoluene.locationservice.services.internal.LanguageInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.main.CountryService;
import com.sabiam.commons.response.AppResponse;

import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryInternalService countryInternalService;
    private final LanguageInternalService languageInternalService;
    private final CurrencyInternalService currencyInternalService;
    private final MessageSourceService messageSourceService;
    private static final String COUNTRY_RETRIEVED = "country.retrieved.successfully";

    @Override
    public Mono<AppResponse<CountryDto>> findCountryByPublicId(UUID publicId) {
        return countryInternalService.findCountryByPublicId(publicId)
                .map(country -> {
                    CountryDto countryDto = CountryHelper.buildCountryDto(country);
                    return new AppResponse<>(HttpStatus.OK.value(),
                            messageSourceService.getMessageByKey(COUNTRY_RETRIEVED),
                            messageSourceService.getMessageByKey(COUNTRY_RETRIEVED),
                            countryDto, null);
                });
    }

    @Override
    public Mono<AppResponse<CountryDto>> findCountryByCode(String code) {
        return countryInternalService.findCountryByThreeLetterCode(code)
                .map(country -> {
                    CountryDto countryDto = CountryHelper.buildCountryDto(country);
                    return new AppResponse<>(HttpStatus.OK.value(),
                            messageSourceService.getMessageByKey(COUNTRY_RETRIEVED),
                            messageSourceService.getMessageByKey(COUNTRY_RETRIEVED),
                            countryDto, null);
                });
    }

    @Override
    public Mono<AppResponse<CountryDto>> findCountryByDialingCode(String dialingCode) {
        return countryInternalService.findCountryByDialingCode(dialingCode)
                .map(country -> {
                    CountryDto countryDto = CountryHelper.buildCountryDto(country);
                    return new AppResponse<>(HttpStatus.OK.value(),
                            messageSourceService.getMessageByKey(COUNTRY_RETRIEVED),
                            messageSourceService.getMessageByKey(COUNTRY_RETRIEVED),
                            countryDto, null);
                });
    }

    @Override
    public Mono<AppResponse<CountryDto>> deactivateCountry(UUID publicId) {
        return countryInternalService.findCountryByPublicId(publicId)
                .flatMap(country -> {
                    country.setStatus(Status.INACTIVE.name());
                    country.setLastModifiedDate(LocalDateTime.now());
                    return countryInternalService.saveCountry(country);
                })
                .map(disableCountry -> {
                    CountryDto countryDto = CountryHelper.buildCountryDto(disableCountry);
                    return new AppResponse<>(HttpStatus.OK.value(),
                            messageSourceService.getMessageByKey("country.disabled.successfully"),
                            messageSourceService.getMessageByKey("country.disabled.successfully"),
                            countryDto, null);
                });
    }

    @Override
    public Mono<AppResponse<CountryDto>> activateCountry(UUID publicId) {
        return countryInternalService.findCountryByPublicId(publicId)
                .flatMap(country -> {
                    country.setStatus(Status.ACTIVE.name());
                    country.setLastModifiedDate(LocalDateTime.now());
                    return countryInternalService.saveCountry(country);
                })
                .map(disableCountry -> {
                    CountryDto countryDto = CountryHelper.buildCountryDto(disableCountry);
                    return new AppResponse<>(HttpStatus.OK.value(),
                            messageSourceService.getMessageByKey("country.enabled.successfully"),
                            messageSourceService.getMessageByKey("country.enabled.successfully"),
                            countryDto, null);
                });
    }

    @Override
    public Mono<AppResponse<PageImpl<CountryDto>>> findAllCountries(int page, int size, String status, String name,
            String sort) {
        return LocationHelper
                .filteringCheck(page, size, status, name, sort, "countryName",
                        messageSourceService.getMessageByKey("page.size.error"))
                .flatMap(f -> filter(f.getPageable(), f.getStatus(), f.getName()));
    }

    @Override
    public Mono<AppResponse<CountryDto>> update(UUID publicId, UpdateCountryDto body) {
        String countryMessageKey = "country.updated.successfully";
        return countryInternalService.findCountryByPublicId(publicId)
                .zipWhen(country -> languagesMeta(body.getLanguages())
                        .map(languageMetaData -> currenciesMeta(body.getCurrencies())
                                .map(currenciesMetaData -> CountryHelper.buildCountrySetting(currenciesMetaData,
                                        languageMetaData))))
                .flatMap(tuple -> tuple.getT2().flatMap(countrySetting -> {
                    tuple.getT1().setCountrySetting(Json.of(countrySetting));
                    return countryInternalService.saveCountry(tuple.getT1());
                })).map(country -> {
                    CountryDto countryDto = CountryHelper.buildCountryDto(country);
                    return new AppResponse<>(HttpStatus.OK.value(),
                            messageSourceService.getMessageByKey(countryMessageKey),
                            messageSourceService.getMessageByKey(countryMessageKey), countryDto, null);
                });
    }

    @Override
    public Mono<AppResponse<List<CountryDto>>> findCountryByPublicIds(List<UUID> ids) {
        return countryInternalService.findCountryByPublicIds(ids.stream().distinct().toList())
                .collectList()
                .flatMap(countries -> {
                    List<CountryDto> countryDtos = countries.stream()
                            .map(CountryHelper::buildCountryDto)
                            .toList();
                    return Mono.just(new AppResponse<>(HttpStatus.OK.value(),
                            messageSourceService.getMessageByKey(COUNTRY_RETRIEVED),
                            messageSourceService.getMessageByKey(COUNTRY_RETRIEVED),
                            countryDtos, null));
                });
    }

    public Mono<List<CountryMetaData>> languagesMeta(List<UUID> uuids) {
        return languageInternalService.findLanguageByPublicId(uuids)
                .map(e -> CountryHelper.buildCountryMeta(e.getName(), e.getId())).collectList();
    }

    public Mono<List<CountryMetaData>> currenciesMeta(List<UUID> uuids) {
        return currencyInternalService.findByPublicId(uuids)
                .map(e -> CountryHelper.buildCountryMeta(e.getName(), e.getId())).collectList();
    }

    private Mono<AppResponse<PageImpl<CountryDto>>> filter(Pageable pageable, String status, String name) {
        Flux<Country> countries;
        Mono<Long> countryCount;
        if (name.isEmpty() && status.isEmpty()) {
            countries = countryInternalService.findAllCountry(pageable);
            countryCount = countryInternalService.count();
        } else if (name.isEmpty()) {
            countries = countryInternalService.findAllByStatus(status, pageable);
            countryCount = countryInternalService.countByStatus(status);
        } else if (status.isEmpty()) {
            countries = countryInternalService.findAllByName(name, pageable);
            countryCount = countryInternalService.countByName(name);
        } else {
            countries = countryInternalService.findAllByStatusAndName(status, name, pageable);
            countryCount = countryInternalService.countByStatusAndName(status, name);
        }

        return countryInternalService.convertCountryToPageable(countries, pageable, countryCount)
                .map(result -> new AppResponse<>(HttpStatus.OK.value(),
                        messageSourceService.getMessageByKey(COUNTRY_RETRIEVED),
                        messageSourceService.getMessageByKey(COUNTRY_RETRIEVED),
                        result, null));
    }
}
