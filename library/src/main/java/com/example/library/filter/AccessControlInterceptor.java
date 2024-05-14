package com.example.library.filter;
import com.auth0.jwt.JWT;
import com.example.library.commom.Result;
import com.example.library.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.stream.Collectors;

public class AccessControlInterceptor implements HandlerInterceptor {
    @Autowired
    private User user;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String header = request.getHeader("content-length");
        System.out.println("请求长度:  "+header);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            stringBuilder.append(reader.lines().collect(Collectors.joining(System.lineSeparator())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("请求内容:  "+ stringBuilder);


        // 获取请求中的所有 Cookie
        try {
            String token = request.getHeader("token");
            String aud = JWT.decode(token).getAudience().get(0);
            Integer userId = Integer.valueOf(aud);

            if (userId != null) {
                return true;
            }
        }
        catch (Exception e) {
            response401(response);
            return false;
        }
        response401(response);
        // 用户信息验证失败，返回 false，并返回错误响应或者重定向到登录页面等操作
        return false;
    }

    // 用户信息验证的方法，可以根据具体业务逻辑来实现
    private boolean validateToken(String token) {
        return user.getToken().equals(token);
    }
    private void response401(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        // 将 ErrorResult 对象转换为 JSON 格式
        String jsonResponse = mapper.writeValueAsString(Result.noAuth());
        response.getWriter().println(jsonResponse);
    }
}
