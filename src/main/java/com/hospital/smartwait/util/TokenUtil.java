package com.hospital.smartwait.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 轻量 Token 工具（认证基础，UC-10）
 *
 * <p>采用内存令牌表：登录成功后签发随机 token 并记录角色，
 * 请求头携带 {@code Authorization: Bearer <token>} 时校验。
 * 课程演示环境使用内存实现，生产环境可替换为 JWT 等方案。
 */
public final class TokenUtil {

    /** token → 角色 */
    private static final Map<String, String> TOKENS = new ConcurrentHashMap<>();

    private TokenUtil() {
    }

    /** 签发 token 并记录角色 */
    public static String issue(String role) {
        String token = UUID.randomUUID().toString().replace("-", "");
        TOKENS.put(token, role);
        return token;
    }

    /** 校验 token 是否有效；有效返回角色，无效返回 null */
    public static String verify(String token) {
        return token == null ? null : TOKENS.get(token);
    }

    /** 注销 token */
    public static void revoke(String token) {
        if (token != null) {
            TOKENS.remove(token);
        }
    }
}
