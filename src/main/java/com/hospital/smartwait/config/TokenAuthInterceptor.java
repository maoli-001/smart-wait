package com.hospital.smartwait.config;

import com.hospital.smartwait.common.Result;
import com.hospital.smartwait.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Token 认证拦截器（UC-10 用户登录认证）
 *
 * <p>除登录接口外，所有接口需携带 Authorization: Bearer &lt;token&gt;，
 * 校验失败返回 401。
 */
@Component
public class TokenAuthInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 放行预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String auth = request.getHeader("Authorization");
        String token = (auth != null && auth.startsWith("Bearer ")) ? auth.substring(7) : null;
        String role = TokenUtil.verify(token);
        if (role == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(Result.error(401, "未登录或登录已过期")));
            return false;
        }
        request.setAttribute("role", role);
        return true;
    }
}
