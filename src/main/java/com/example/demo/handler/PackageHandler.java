package com.example.demo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PackageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageHandler.class);
    public Mono<ServerResponse> installed(ServerRequest request) {
        return request.bodyToMono(InstalledRequest.class)
                .flatMap(installedRequest -> {
                    LOGGER.info("package installed. headers => {}", request.headers());

                    LOGGER.info("package installed. detail => {}", installedRequest);
                    return Mono.just("accepted");
                })
                .flatMap(result ->
                        ServerResponse.ok().body(Mono.just(result), String.class));
    }

    public Mono<ServerResponse> uninstalled(ServerRequest request) {
        return request.bodyToMono(UninstalledRequest.class)
                .flatMap(uninstalledRequest -> {
                    LOGGER.info("package uninstalled. detail => {}", uninstalledRequest);
                    return Mono.just("accepted");
                })
                .flatMap(result ->
                        ServerResponse.ok().body(Mono.just(result), String.class));
    }

    public record InstalledRequest(
            String organizationId,
            String installerId,
            boolean isUpgrade,
            boolean isPush,
            String version
    ) {}

    public record UninstalledRequest(
            String organizationId,
            String uninstallerId
    ) {}
}
