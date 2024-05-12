package com.demo.gateway.exception;

import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class CustomErrorWebExceptionHandler {

    @Bean
    public ErrorWebExceptionHandler errorWebExceptionHandler123(ErrorAttributes errorAttributes) {
        return (exchange, ex) -> {
            if (ex instanceof ResponseStatusException) {
                ResponseStatusException responseStatusException = (ResponseStatusException) ex;
                exchange.getResponse().setStatusCode(responseStatusException.getStatus());
                return exchange.getResponse().setComplete();
            }
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        };
    }
}
