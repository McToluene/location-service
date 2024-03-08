package com.mctoluene.locationservice.services.main;

import com.mctoluene.locationservice.domains.requestdtos.CurrencyRequestDto;
import com.mctoluene.locationservice.domains.requestdtos.StateProvinceRequestDto;
import com.mctolueneam.commons.response.AppResponse;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CurrencyService {
    Mono<AppResponse> createCurrency(CurrencyRequestDto currencyRequestDto);

    Mono<AppResponse> findAllCurrency(int page, int size);
}
