package com.example.demo.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void hello() {
        webTestClient.get()
                .uri("/demo/hello")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Hello by flux");

        /* NOTE: other way
        FluxExchangeResult<String> result = webTestClient.get()
                .uri("/greet/hello")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class);

        StepVerifier.create(result.getResponseBody())
                .expectNextMatches(value -> value.equals("Hello by flux"))
                .expectComplete()
                .verify();
         */
    }
}