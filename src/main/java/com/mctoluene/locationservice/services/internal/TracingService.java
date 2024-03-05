package com.mctoluene.locationservice.services.internal;

import brave.baggage.BaggageField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TracingService {

    private final BaggageField trackingCodeTraceField;

    public void propagateSleuthFields(String trackingCode) {
        trackingCodeTraceField.updateValue(trackingCode);
    }
}
