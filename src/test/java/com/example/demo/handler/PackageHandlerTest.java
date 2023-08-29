package com.example.demo.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PackageHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void installed() {
        webTestClient.post()
                .uri("/package/installed")
                .bodyValue(new PackageHandler.InstalledRequest("foo", "04xxxxxx", false, false, "1.2.3"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("accepted");
    }

    @Test
    void uninstalled() {
        webTestClient.post()
                .uri("/package/uninstalled")
                .bodyValue(new PackageHandler.UninstalledRequest("foo", "04xxxxxx"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("accepted");
    }

}