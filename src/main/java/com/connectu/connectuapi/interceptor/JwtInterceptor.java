package com.connectu.connectuapi.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;

public class JwtInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String SIGNING_KEY = "123";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 获取HTTP请求的Authorization头部
        String token = request.getHeader(AUTHORIZATION_HEADER);

        // 如果token存在，验证token
        if (token != null) {
            boolean result = JWTUtil.verify(token, SIGNING_KEY.getBytes(StandardCharsets.UTF_8));
            if (result) {
                // 如果验证成功，继续处理请求
                return true;
            }
        }

        // 如果验证失败，返回HTTP 401 Unauthorized状态
        request.getSession().invalidate();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // 请求被处理后，但在渲染视图之前调用
        // 在这里可以添加一些逻辑，例如记录日志等
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 请求完全处理完后调用，即在视图被渲染后
        // 在这里可以添加一些逻辑，例如清理资源，记录日志等
    }
}
