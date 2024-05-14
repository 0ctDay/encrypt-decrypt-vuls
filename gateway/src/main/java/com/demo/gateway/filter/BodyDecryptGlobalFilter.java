package com.demo.gateway.filter;


import com.demo.gateway.utils.AESUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


/**
 * 请求体 Body 解密 过滤器
 *
 * @author xuxiaowei
 * @see ServerHttpRequestDecorator
 * @see <a href="https://stackoverflow.com/questions/66822340/spring-webflux-security-and-request-body">spring-webflux-security-and-request-body</a>
 * @since 0.0.1
 */
@Component
@Configuration
public class BodyDecryptGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return 10;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();


        return DataBufferUtils.join(request.getBody())
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);



                    String bodyString = new String(bytes);
                    // 这里可以对bodyString进行任何你需要的操作，比如日志记录或修改内容
                    System.out.println("请求解密前: " + bodyString);

                    String decrypt = AESUtil.decrypt(bodyString);


                    // 重新创建一个新的请求对象
                    Flux<DataBuffer> bodyFlux = Flux.defer(() -> {
                        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(decrypt.getBytes());
                        System.out.println("请求解密后: " + decrypt);
                        return Mono.just(buffer);
                    });

                    if (request.getMethod() == HttpMethod.GET) {
                        return chain.filter(exchange);
                    }

                    ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(request) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return bodyFlux;
                        }

                        @NonNull
                        @Override
                        public HttpHeaders getHeaders() {
                            HttpHeaders headers = new HttpHeaders();
                            headers.addAll(super.getHeaders());
                            headers.remove(HttpHeaders.CONTENT_LENGTH);
                            headers.setContentLength(decrypt.length());
                            return headers;
                        }
                    };

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                });


    }

}