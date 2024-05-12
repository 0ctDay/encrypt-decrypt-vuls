package com.demo.gateway.filter;

import com.demo.gateway.annotations.LoggableGlobalFilter;
import com.demo.gateway.utils.AESUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.nio.charset.StandardCharsets;

/**
 * 响应 Body 加密 过滤器
 *
 * @author xuxiaowei
 * @see ServerHttpResponseDecorator
 * @since 0.0.1
 */

@Configuration
@Component
public class ResponseEncryptionGlobalFilter implements GlobalFilter, Ordered {
    private static final String AES_SECURTY = "MTIzNDU2Nzg5MTIzNDU2Nw==";//1234567891234567
    private static final Logger log = LogManager.getLogger();
    /**
     * 加密 过滤器 优先级
     * <p>
     * 响应数据过滤器优先级需要小于 0，否则将无效
     */
    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("触发");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        ServerHttpResponseDecorator decorator = new ServerHttpResponseDecorator(exchange.getResponse()) {

            @NonNull
            @Override
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                @SuppressWarnings("unchecked")
                Flux<? extends DataBuffer> fluxDataBuffer = (Flux<? extends DataBuffer>) body;
                return super.writeWith(fluxDataBuffer.buffer()
                        .map(dataBuffer -> {
                            DataBuffer join = exchange.getResponse().bufferFactory().join(dataBuffer);
                            byte[] bytes = new byte[join.readableByteCount()];
                            join.read(bytes);
                            DataBufferUtils.release(join);

                            System.out.println("加密前 body："+ new String(bytes));

                            byte[] encryption;

                            try {
                            //加密
                                encryption = AESUtil.encrypt(new String(bytes), AES_SECURTY).getBytes();


                            } catch (Exception e) {
                                encryption = bytes;
                                log.error("数据类型不是 JSON，不加密", e);
                            }

                            System.out.println("加密后 body："+ new String(encryption, StandardCharsets.UTF_8));
                            HttpHeaders headers = getDelegate().getHeaders();
                            headers.setContentLength(encryption.length);
                            return exchange.getResponse().bufferFactory().wrap(encryption);
                        })
                );
            }

        };

        return chain.filter(exchange.mutate().response(decorator).build());
    }

}




