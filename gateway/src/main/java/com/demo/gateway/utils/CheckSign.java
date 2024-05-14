package com.demo.gateway.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.Md5Utils;
import com.demo.gateway.config.FilterUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class CheckSign {
    private static final String ERROR_MESSAGE = "拒绝服务";
    private static final String SIGN_ERROR_MESSAGE = "签名过期";
    public Map<String, Object> paramMap;
    public CheckSign(Map map){
        this.paramMap = map;
    }
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    public boolean Sign(ServerWebExchange exchange){

        //1 获取时间戳
        Long dateTimestamp = getDateTimestamp(exchange.getRequest().getHeaders());
        //2 获取RequestId
        String requestId = getRequestId(exchange.getRequest().getHeaders());
        //3 获取签名
        String sign = getSign(exchange.getRequest().getHeaders());
        //4 获取URL参数

        checkSign(sign, dateTimestamp, requestId, paramMap);
        //5 获取请求体参数
        return true;
    }

    public void checkSign(String sign, Long dateTimestamp, String requestId, Map<String, Object> paramMap) {
        String str = JSON.toJSONString(paramMap) + requestId + dateTimestamp;
        String tempSign = Md5Utils.getMD5(str.getBytes());
        if (!tempSign.equals(sign)) {
            throw new IllegalArgumentException(SIGN_ERROR_MESSAGE);
        }
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
}
