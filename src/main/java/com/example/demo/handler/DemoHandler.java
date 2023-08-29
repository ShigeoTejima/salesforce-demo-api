package com.example.demo.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class DemoHandler {

    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().body(Mono.just("Hello by flux"), String.class);
    }
}
