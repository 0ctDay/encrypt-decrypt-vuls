package com.demo.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.Md5Utils;
import com.demo.gateway.annotations.LoggableGlobalFilter;
import com.demo.gateway.config.FilterUtils;
import com.demo.gateway.pojo.MyCachedBodyOutputMessage;
import com.demo.gateway.utils.AESUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;


/**
 * @Description:
 * @Author: rosh
 * @Date: 2021/10/26 22:24
 */
@Configuration
@Component
public class RequestEncryptionGlobalFilter implements GlobalFilter, Ordered {

    private static final String AES_SECURTY = "MTIzNDU2Nzg5MTIzNDU2Nw==";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final Logger log = LogManager.getLogger();

    private static final String ERROR_MESSAGE = "拒绝服务";
    private static final String SIGN_ERROR_MESSAGE = "签名过期";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //拦截OPTIONS请求
        System.out.println("1. 获取请求头");
        //1 获取时间戳
        Long dateTimestamp = getDateTimestamp(exchange.getRequest().getHeaders());
        //2 获取RequestId
        String requestId = getRequestId(exchange.getRequest().getHeaders());
        //3 获取签名
        String sign = getSign(exchange.getRequest().getHeaders());
        //4 如果是登录不校验Token
//        String requestUrl = exchange.getRequest().getPath().value();
//        AntPathMatcher pathMatcher = new AntPathMatcher();
//        if (!pathMatcher.match("/user/login", requestUrl)) {
//            String token = exchange.getRequest().getHeaders().getFirst(UserConstant.TOKEN);
//            Claims claim = TokenUtils.getClaim(token);
//            if (StringUtils.isBlank(token) || claim == null) {
//                return FilterUtils.invalidToken(exchange);
//            }
//        }
        //5 修改请求参数,并获取请求参数

        Map<String, Object> paramMap;
        try {
            paramMap = updateRequestParam(exchange);
        } catch (Exception e) {
            return FilterUtils.invalidUrl(exchange);
        }
        System.out.println("2. 获取请求参数: "+ JSON.toJSON(paramMap));
        //6 获取请求体,修改请求体
        ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
            String encrypt = AESUtil.decrypt(body, AES_SECURTY);
            JSONObject jsonObject = JSON.parseObject(encrypt);
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                paramMap.put(entry.getKey(), entry.getValue());
            }
            checkSign(sign, dateTimestamp, requestId, paramMap);
            System.out.println("3. 修改请求体:"+encrypt);
            return Mono.just(encrypt);
        });

        //创建BodyInserter修改请求体

        BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        //创建CachedBodyOutputMessage并且把请求param加入,初始化校验信息
        MyCachedBodyOutputMessage outputMessage = new MyCachedBodyOutputMessage(exchange, headers);
        outputMessage.initial(paramMap, requestId, sign, dateTimestamp);


        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    Flux<DataBuffer> body = outputMessage.getBody();
                    if (body.equals(Flux.empty())) {
                        //验证签名
                        checkSign(outputMessage.getSign(), outputMessage.getDateTimestamp(), outputMessage.getRequestId(), outputMessage.getParamMap());
                    }
                    System.out.println("4. 完成");
                    return outputMessage.getBody();

                }
            };

            return chain.filter(exchange.mutate().request(decorator).build());
        }));
    }

    public void checkSign(String sign, Long dateTimestamp, String requestId, Map<String, Object> paramMap) {
        String str = JSON.toJSONString(paramMap) + requestId + dateTimestamp;
        String tempSign = Md5Utils.getMD5(str.getBytes());
        if (!tempSign.equals(sign)) {
            throw new IllegalArgumentException(SIGN_ERROR_MESSAGE);
        }
    }

    /**
     * 修改前端传的参数
     */
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


    private String getSign(HttpHeaders headers) {
        List<String> list = headers.get("sign");
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        return list.get(0);
    }

    private Long getDateTimestamp(HttpHeaders httpHeaders) {
        List<String> list = httpHeaders.get("timestamp");
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        long timestamp = Long.parseLong(list.get(0));
        long currentTimeMillis = System.currentTimeMillis();
        //有效时长为5分钟
        if (currentTimeMillis - timestamp > 1000 * 60 * 5) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        return timestamp;
    }

    private String getRequestId(HttpHeaders headers) {
        List<String> list = headers.get("requestId");
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        String requestId = list.get(0);
        //如果requestId存在redis中直接返回
        String temp = redisTemplate.opsForValue().get(requestId);
        if (StringUtils.isNotBlank(temp)) {
            throw new IllegalArgumentException("RequestId 非法");
        }
        redisTemplate.opsForValue().set(requestId, requestId, 5, TimeUnit.MINUTES);
        return requestId;
    }


    @Override
    public int getOrder() {
        return 80;
    }
}
