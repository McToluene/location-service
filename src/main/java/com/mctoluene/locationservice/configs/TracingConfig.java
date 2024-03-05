package com.mctoluene.locationservice.configs;

import brave.baggage.BaggageField;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TracingConfig {

    @Bean
    @Qualifier("traceId")
    public BaggageField traceIdTraceField() {
        return BaggageField.create("traceId");
    }

}
