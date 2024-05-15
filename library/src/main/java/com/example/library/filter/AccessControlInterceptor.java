package com.example.library.filter;

import com.auth0.jwt.JWT;
import com.example.library.commom.Result;
import com.example.library.entity.User;
import com.example.library.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AccessControlInterceptor implements HandlerInterceptor {
    @Autowired
    private User user;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // 获取请求中的所有 Cookie
        try {
            String token = request.getHeader("token");
            String aud = TokenUtils.checkToken(token).getAudience().get(0);
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

    private static byte[] toByteArray(ServletInputStream input) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }
}
