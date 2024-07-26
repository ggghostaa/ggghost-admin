package com.ggghost.framework.utlis;

import com.ggghost.framework.dto.LoginUser;
import com.ggghost.framework.entity.SysUser;
import io.jsonwebtoken.*;
import jdk.swing.interop.SwingInterOpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-11
 * @Description: jwt令牌工具类
 * @Version: 1.0
 */
@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    private String SECRET = "Bzv9J88NUdgwAvKWi6s9h3ZbrXfd9D";

    private long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    private long REFRESH_TIME = 1000 * 30 * 60 * 12;

    private final String issuer = "ggghost";

    private SecretKey secretKey;

    {
        secretKey = Jwts.SIG.HS256.key()
                .random(new SecureRandom(SECRET.getBytes(StandardCharsets.UTF_8))).build();
    }


    /**

     * @param user
     * @param user
     * @return
     */
    public String generateToken(LoginUser user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        return encoding(claims, EXPIRATION_TIME);
    }
    /**
     * 二次加密
     * @param encoding
     * @param loginId
     * @return 下表
     */
    public String generateToken(String encoding, String loginId) {
        HashMap<String, Object> claims = new HashMap<>();
        claims = new HashMap<>();
        claims.put("loginId", loginId);
        claims.put("x-token", encoding);
        return encoding(claims);
    }


    /**
     *
     * @param claims
     * @param expiration
     * @return
     */
    public String encoding(Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .issuedAt(new Date())
                .issuer(issuer)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }
    /**
     * 生成token
     * @param claims
     * @return
     */
    public String encoding(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .issuedAt(new Date())
                .issuer(issuer)
                .compact();
    }

    /**
     * 获取荷载
     * @return
     */
    public Claims decoding(String token) {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

    /**
     * token是否过期
     * @param token
     * @return
     */
    public boolean isExpired(String token) {
        try {
            Claims decoding = decoding(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    /**
     * 是否需要刷新token
     * @param token
     * @return
     */
    public boolean isRefresh(String token) {
        Claims decoding = null;
        try {
            decoding = decoding(token);
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            log.error("token error\n\r {}", e.getMessage());
            throw e;
        }
        Date expiration = decoding.getExpiration();
        return (expiration.getTime() - System.currentTimeMillis()) < REFRESH_TIME;
    }


    /**
     * 获取token自定义值
     * @param token
     * @param filed
     * @return
     */
    public String getClaimFiled(String token, String filed) {
        try {
            Claims claims = decoding(token);
            return (String) claims.get(filed);
        } catch (Exception e) {
            return null;
        }
    }

}