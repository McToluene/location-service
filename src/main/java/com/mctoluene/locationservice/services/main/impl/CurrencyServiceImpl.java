package com.mctoluene.locationservice.services.main.impl;

import com.mctoluene.locationservice.domains.dtos.CurrencyDto;
import com.mctoluene.locationservice.domains.requestdtos.CurrencyRequestDto;
import com.mctoluene.locationservice.helpers.CurrencyHelper;
import com.mctoluene.locationservice.models.Currency;
import com.mctoluene.locationservice.services.internal.CurrencyInternalService;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.main.CurrencyService;
import com.sabiam.commons.exceptions.ConflictException;
import com.sabiam.commons.response.AppResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyInternalService currencyInternalService;
    private final MessageSourceService messageSourceService;

    @Override
    public Mono<AppResponse> createCurrency(CurrencyRequestDto currencyRequestDto) {
        return currencyInternalService.findByName(currencyRequestDto.getName())
                .flatMap(foundCurrency -> Mono.error(new ConflictException(messageSourceService
                        .getMessageByKey("currency.already.exist.error"))))
                .switchIfEmpty(saveNewCurrency(currencyRequestDto))
                .map(currency1 -> {
                    CurrencyDto currencyDto = CurrencyHelper.buildCurrencyDto((Currency) currency1);
                    return new AppResponse(HttpStatus.CREATED.value(),
                            messageSourceService.getMessageByKey("currency.created.successfully"),
                            messageSourceService.getMessageByKey("currency.created.successfully"),
                            currencyDto, null);
                });
    }

    @Override
    public Mono<AppResponse> findAllCurrency(int page, int size) {
        page = (page <= 0) ? 0 : page - 1;
        size = (size <= 0) ? 0 : size;
        PageRequest pageRequest = PageRequest.of(page, size < 1 ? 10 : size);
        return currencyInternalService.getAllCurrencies(pageRequest)
                .map(CurrencyHelper::buildCurrencyDto)
                .collectList()
                .zipWith(currencyInternalService.countByStatus())
                .map(response -> new AppResponse(HttpStatus.OK.value(),
                        messageSourceService.getMessageByKey("currency.fetched.successfully"),
                        messageSourceService.getMessageByKey("currency.fetched.successfully"),
                        new PageImpl<>(response.getT1(), pageRequest, response.getT2()), null));
    }

    private Mono<Currency> saveNewCurrency(CurrencyRequestDto currencyRequestDto) {
        Currency currency = CurrencyHelper.buildCurrency(currencyRequestDto);
        return currencyInternalService.saveCurrency(currency);
    }
}
