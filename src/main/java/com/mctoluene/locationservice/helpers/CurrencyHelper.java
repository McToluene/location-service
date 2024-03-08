package com.mctoluene.locationservice.helpers;

import com.mctoluene.locationservice.domains.dtos.CurrencyDto;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.domains.requestdtos.CurrencyRequestDto;
import com.mctoluene.locationservice.models.Currency;
import com.mctolueneam.commons.response.AppResponse;

import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

public class CurrencyHelper {

    private CurrencyHelper() {
    }

    public static CurrencyDto buildCurrencyDto(Currency currency) {
        return new CurrencyDto(currency.getPublicId(), currency.getCode(), currency.getName(),
                currency.getCreatedDate(), currency.getLastModifiedDate(), currency.getCreatedBy(),
                currency.getLastModifiedBy(), currency.getStatus());
    }

    public static Currency buildCurrency(CurrencyRequestDto currencyRequestDto) {
        Currency currency = Currency.builder()
                .publicId(UUID.randomUUID())
                .name(currencyRequestDto.getName())
                .build();
        currency.setStatus(Status.ACTIVE.name());
        currency.setCode(currencyRequestDto.getCode());
        currency.setCreatedBy(currencyRequestDto.getCreatedBy());
        currency.setCreatedDate(LocalDateTime.now());
        return currency;
    }

    public static ResponseEntity<AppResponse> buildCurrencyResponse(AppResponse response) {
        CurrencyDto currencyDto = (CurrencyDto) response.getData();
        return ResponseEntity.created(URI
                .create(String.format("/api/v1/location/currency/%s", currencyDto.publicId()))).body(response);
    }

}
