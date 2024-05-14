package com.demo.gateway.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.gateway.config.FilterUtils;
import com.demo.gateway.utils.AESUtil;
import com.demo.gateway.utils.CheckSign;
import org.apache.commons.lang3.StringUtils;
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

import java.net.URI;
import java.util.Map;
import java.util.TreeMap;


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

        CheckSign sign;
        //获取请求参数
        try {
            sign = new CheckSign(updateRequestParam(exchange));
        } catch (Exception e) {
            return FilterUtils.invalidUrl(exchange);
        }
        //处理GET请求
        if (request.getMethod() == HttpMethod.GET) {
            //验证签名
            if(!sign.Sign(exchange)){
                return FilterUtils.invalidUrl(exchange);
            }
            return chain.filter(exchange);
        }

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

                    JSONObject jsonObject = JSON.parseObject(decrypt);
                    for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                        sign.paramMap.put(entry.getKey(), entry.getValue());
                    }
                    if(!sign.Sign(exchange)){
                        return FilterUtils.invalidUrl(exchange);
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
    private Map<String, Object> updateRequestParam(ServerWebExchange exchange) throws NoSuchFieldException, IllegalAccessException {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String query = uri.getQuery();
        if (StringUtils.isNotBlank(query) && query.contains("param")) {
            return getParamMap(query);
        }
        return new TreeMap<>();
    }
    private Map<String, Object> getParamMap(String param) {
        Map<String, Object> map = new TreeMap<>();
        String[] split = param.split("&");
        for (String str : split) {
            String[] params = str.split("=");
            map.put(params[0], params[1]);
        }
        return map;
    }

}