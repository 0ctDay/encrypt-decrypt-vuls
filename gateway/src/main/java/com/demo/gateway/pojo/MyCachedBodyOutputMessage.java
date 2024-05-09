package com.demo.gateway.pojo;

import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

/**
 * @Description:
 * @Author: Rosh
 * @Date: 2021/10/27 11:03
 */
public class MyCachedBodyOutputMessage extends CachedBodyOutputMessage {

    private Map<String, Object> paramMap;

    private Long dateTimestamp;

    private String requestId;

    private String sign;

    public MyCachedBodyOutputMessage(ServerWebExchange exchange, HttpHeaders httpHeaders) {
        super(exchange, httpHeaders);
    }

    public void initial(Map<String, Object> paramMap, String requestId, String sign, Long dateTimestamp) {
        this.paramMap = paramMap;
        this.requestId = requestId;
        this.sign = sign;
        this.dateTimestamp = dateTimestamp;
    }


    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public Long getDateTimestamp() {
        return dateTimestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getSign() {
        return sign;
    }
}
