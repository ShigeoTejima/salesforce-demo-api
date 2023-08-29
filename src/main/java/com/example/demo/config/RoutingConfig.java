package com.example.demo.config;

import com.example.demo.handler.ContractHandler;
import com.example.demo.handler.DemoHandler;
import com.example.demo.handler.PackageHandler;
import com.example.demo.model.Contract;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class RoutingConfig {

    @Bean
    public RouterFunction<ServerResponse> demoRoutes(DemoHandler handler) {
        return route()
                .GET("/demo/hello", handler::hello)
                .build();
    }

    @RouterOperations({
        @RouterOperation(
            path = "/package/installed",
            method = {RequestMethod.POST},
            operation = @Operation(
                tags = {"package-handler"},
                operationId = "installed",
                method = "POST",
                requestBody = @RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PackageHandler.InstalledRequest.class))),
                responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(implementation = String.class)))
                }
            )
        ),
        @RouterOperation(
            path = "/package/uninstalled",
            method = {RequestMethod.POST},
            operation = @Operation(
                tags = {"package-handler"},
                operationId = "uninstalled",
                method = "POST",
                requestBody = @RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PackageHandler.UninstalledRequest.class))),
                responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(implementation = String.class)))
                }
            )
        )
    })
    @Bean
    public RouterFunction<ServerResponse> packageRoutes(PackageHandler handler) {
        return route()
                .POST("/package/installed",
                        RequestPredicates.contentType(MediaType.APPLICATION_JSON),
                        handler::installed)
                .POST("/package/uninstalled",
                        RequestPredicates.contentType(MediaType.APPLICATION_JSON),
                        handler::uninstalled)
                .build();
//        return route(GET("/greet/hello"), greetController::hello);
    }

    @RouterOperations({
        @RouterOperation(
            path = "/contract",
            method = {RequestMethod.GET},
            operation = @Operation(
                tags = {"contract-handler"},
                operationId = "getContractByAuth",
                method = "GET",
                parameters = {
                    @Parameter(name = "api-key", in = ParameterIn.HEADER, required = true),
                    @Parameter(name = "organization-id", in = ParameterIn.HEADER, required = true)
                },
                responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Contract.class))),
                    @ApiResponse(responseCode = "401")
                }
            )
        )
    })
    @Bean
    public RouterFunction<ServerResponse> contractRoutes(ContractHandler handler) {
        return route()
                .GET("/contract",
                    RequestPredicates.accept(MediaType.APPLICATION_JSON)
                        .and(RequestPredicates.headers(headers ->
                            Optional.ofNullable(headers.firstHeader("api-key"))
                                    .map(v -> !v.isEmpty())
                                    .orElse(false) &&
                            Optional.ofNullable(headers.firstHeader("organization-id"))
                                    .map(v -> !v.isEmpty())
                                    .orElse(false))),
                    handler::getContractByAuth)
                .build();
    }
}
