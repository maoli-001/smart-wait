package com.hospital.smartwait.controller;

import com.hospital.smartwait.common.Result;
import com.hospital.smartwait.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 认证接口（UC-10 用户登录认证）
 *
 * <p>接口清单：
 *  POST /api/auth/login    登录签发 token
 *  POST /api/auth/logout   注销 token
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** // @UseCase UC-10 登录认证 */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        return Result.ok(authService.login(
                body.get("role"),
                body.get("username"),
                body.get("password")));
    }

    /** // @UseCase UC-10 注销 */
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = (auth != null && auth.startsWith("Bearer ")) ? auth.substring(7) : null;
        authService.logout(token);
        return Result.ok();
    }
}
