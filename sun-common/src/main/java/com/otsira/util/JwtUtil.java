package com.otsira.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用于生成 jwt token 的工具类
 * @create: 2024/10/20 20:11
 */
public class JwtUtil {
    /**
     * 生成 jwt
     * 使用 Hs256 算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt 秘钥
     * @param ttlMillis jwt 过期时间(毫秒)
     * @param claims    设置的信息
     * @return jwt token
     */
    public static String generateToken(String secretKey, long ttlMillis, HashMap<String, Object> claims) {
        // 指定签名的时候使用的签名算法，也就是 header 那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 生成 JWT 的时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明
                // 这个是给 builder 的 claim 赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(exp);
        return builder.compact();
    }

    /**
     * Token 解密
     *
     * @param secretKey jwt 秘钥
     * @param token     加密后的 token
     * @return 解密后的 token
     */
    public static Claims parseToken(String secretKey, String token) {
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置需要解析的 jwt
                .parseClaimsJws(token)
                .getBody();
    }
}
