package com.example.demo.handler;

import com.example.demo.model.Contract;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "app.demo.contract.id=1",
        "app.demo.contract.name=contract name",
        "app.demo.contract.api-key=api-key-of-contract",
        "app.demo.contract.organization-id=organization-id-of-contract"
    }
)
class ContractHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Nested
    class GetContractByAuth {

        @Test
        void ok() {
            webTestClient.get()
                .uri("/contract")
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", "api-key-of-contract")
                .header("organization-id", "organization-id-of-contract")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Contract.class)
                .isEqualTo(new Contract("1", "contract name"));
        }

        @Test
        void acceptIsNotCorrect() {
            webTestClient.get()
                .uri("/contract")
                .header("Accept", MediaType.TEXT_PLAIN_VALUE)
                .header("api-key", "api-key-of-contract")
                .header("organization-id", "organization-id-of-contract")
                .exchange()
                .expectStatus().isNotFound();
        }

        @Test
        void whenApiKeyNotSpecified() {
            webTestClient.get()
                .uri("/contract")
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .header("organization-id", "organization-id-of-contract")
                .exchange()
                .expectStatus().isNotFound();
        }

        @Test
        void whenApiKeyIsEmpty() {
            webTestClient.get()
                .uri("/contract")
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", "")
                .header("organization-id", "organization-id-of-contract")
                .exchange()
                .expectStatus().isNotFound();
        }

        @Test
        void whenOrganizationIdNotSpecified() {
            webTestClient.get()
                .uri("/contract")
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", "api-key-of-contract")
                .exchange()
                .expectStatus().isNotFound();
        }

        @Test
        void whenOrganizationIdIsEmpty() {
            webTestClient.get()
                .uri("/contract")
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", "api-key-of-contract")
                .header("organization-id", "")
                .exchange()
                .expectStatus().isNotFound();
        }

        @Test
        void whenApiKeyIsNotCorrect() {
            webTestClient.get()
                .uri("/contract")
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", "not-correct-api-key")
                .header("organization-id", "organization-id-of-contract")
                .exchange()
                .expectStatus().isUnauthorized();
        }

        @Test
        void whenOrganizationIdIsNotCorrect() {
            webTestClient.get()
                .uri("/contract")
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", "api-key-of-contract")
                .header("organization-id", "not-correct-organization-id")
                .exchange()
                .expectStatus().isUnauthorized();
        }
    }

}