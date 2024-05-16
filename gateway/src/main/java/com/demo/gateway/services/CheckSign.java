package com.demo.gateway.services;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CheckSign {
    private static final String ERROR_MESSAGE = "拒绝服务";
    private static final String SIGN_ERROR_MESSAGE = "签名过期";
    public Map<String, Object> paramMap;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    public void Sign(ServerHttpRequest request){

        //1 获取时间戳
        Long dateTimestamp = getDateTimestamp(request.getHeaders());
        //2 获取RequestId
        String requestId = getRequestId(request.getHeaders());
        //3 获取签名
        String sign = getSign(request.getHeaders());
        //4 获取URL参数
        checkSign(sign, dateTimestamp, requestId, paramMap);

    }

    public void checkSign(String sign, Long dateTimestamp, String requestId, Map<String, Object> paramMap) {
        String str = JSON.toJSONString(paramMap) + requestId + dateTimestamp;
        //打印签名
        System.out.println(str);
        String tempSign = DigestUtils.md5Hex(str);
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
