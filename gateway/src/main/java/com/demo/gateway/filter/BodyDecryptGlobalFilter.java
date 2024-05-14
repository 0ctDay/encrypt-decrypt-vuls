package com.demo.gateway.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

/**
 * 请求体 Body 解密 过滤器
 *
 * @author xuxiaowei
 * @see ServerHttpRequestDecorator
 * @see <a href="https://stackoverflow.com/questions/66822340/spring-webflux-security-and-request-body">spring-webflux-security-and-request-body</a>
 * @since 0.0.1
 */
//@Component
public class BodyDecryptGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {

            @NonNull
            @Override
            public Flux<DataBuffer> getBody() {
                return super.getBody().buffer()
                        .map(dataBuffer -> {
                            DataBuffer join = exchange.getResponse().bufferFactory().join(dataBuffer);
                            byte[] bytes = new byte[join.readableByteCount()];
                            join.read(bytes);
                            DataBufferUtils.release(join);

                            System.out.println("解密前 body：{}"+ new String(bytes));

                            byte[] decrypt;

                            try {

                                @SuppressWarnings("unchecked")
                                Map<String, Object> map = objectMapper.readValue(bytes, Map.class);

                                map.put("test", "数据已解密（仅演示，解密方式，自己实现）");

                                decrypt = objectMapper.writeValueAsBytes(map);

                            } catch (IOException e) {
                                decrypt = bytes;
                                System.out.println("数据类型不是 JSON，不解密" + e);
                            }

                            System.out.println("解密后 body：{}" + new String(decrypt));

                            return exchange.getResponse().bufferFactory().wrap(decrypt);
                        });
            }

            @NonNull
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.addAll(super.getHeaders());
                headers.remove(HttpHeaders.CONTENT_LENGTH);
                return headers;
            }

        };

        return chain.filter(exchange.mutate().request(decorator).build());
    }

}