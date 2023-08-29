package com.example.demo.handler;

import com.example.demo.model.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class ContractHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContractHandler.class);

    /*
     * NOTE: デモ契約の情報を設定から取得できるようにした.
     *       必要になったらリポジトリを用意する.
     */
    private final String demoId;
    private final String demoName;
    private final String demoApiKey;
    private final String demoOrganizationId;

    public ContractHandler(
        @Value("${app.demo.contract.id}") String demoId,
        @Value("${app.demo.contract.name}") String demoName,
        @Value("${app.demo.contract.api-key}") String demoApiKey,
        @Value("${app.demo.contract.organization-id}") String demoOrganizationId
    ) {
        this.demoId = demoId;
        this.demoName = demoName;
        this.demoApiKey = demoApiKey;
        this.demoOrganizationId = demoOrganizationId;
    }

    public Mono<ServerResponse> getContractByAuth(ServerRequest request) {
        LOGGER.info("getContractByAuth - request: {}", request);
        LOGGER.info("getContractByAuth - headers: {}", request.headers());
        String apiKey = request.headers().firstHeader("api-key");
        // NOTE: contractRoutes()でヘッダーがない場合をチェックしていて、ここの判定に引っかからない
        if (Objects.isNull(apiKey) || apiKey.isEmpty()) {
            throw new IllegalArgumentException("apiKey required.");
        }

        String organizationId = request.headers().firstHeader("organization-id");
        // NOTE: contractRoutes()でヘッダーがない場合をチェックしていて、ここの判定に引っかからない
        if (Objects.isNull(organizationId) || organizationId.isEmpty()) {
            throw new IllegalArgumentException("organizationId required.");
        }

        if (apiKey.equals(demoApiKey) && organizationId.equals(demoOrganizationId)) {
            return ServerResponse.ok().body(Mono.just(new Contract(demoId, demoName)), Contract.class);
        } else {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
