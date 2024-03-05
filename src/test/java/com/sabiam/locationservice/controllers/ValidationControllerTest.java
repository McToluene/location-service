package com.sabiam.locationservice.controllers;

import com.mctoluene.locationservice.controllers.ValidationController;
import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.mctoluene.locationservice.services.internal.TracingService;
import com.mctoluene.locationservice.services.main.ValidateCountryStateLgaService;
import com.sabiam.commons.response.AppResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ValidationController.class)
class ValidationControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ValidateCountryStateLgaService validationService;

    @MockBean
    TracingService tracingService;

    @MockBean
    MessageSourceService messageSourceService;

    @Test
    void validateCountryStateLga() {

        UUID statePublicId = UUID.fromString("b8895bfd-13a7-467a-b2e4-8df57b62fe15");
        UUID countryPublicId = UUID.fromString("bc8b00d0-2aaf-4768-95da-d6f0d9f7be64");
        UUID localGovernmentPublicId = UUID.fromString("1ba67b57-016b-49a8-9a8b-bb04efc51d93");

        Mono<AppResponse<List<String>>> response = Mono.just(new AppResponse<>(HttpStatus.OK.value(),
                messageSourceService.getMessageByKey("country.state.lga.validated.successfully"),
                messageSourceService.getMessageByKey("country.state.lga.validated.successfully"),
                List.of("Country province public id: bc8b00d0-2aaf-4768-95da-d6f0d9f7be64",
                        "Local Government public id: 1ba67b57-016b-49a8-9a8b-bb04efc51d93",
                        "State province public id: b8895bfd-13a7-467a-b2e4-8df57b62fe15"),
                null));

        when(validationService.validateCountryStateLga(countryPublicId, statePublicId, localGovernmentPublicId))
                .thenReturn(response);

        String uri = "/api/v1/location/validate?countryPublicId=bc8b00d0-2aaf-4768-95da-d6f0d9f7be64&statePublicId=b8895bfd-13a7-467a-b2e4-8df57b62fe15&localGovernmentPublicId=1ba67b57-016b-49a8-9a8b-bb04efc51d93";
        webTestClient
                .get()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("x-trace-id", UUID.randomUUID().toString())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(200);
    }

}
