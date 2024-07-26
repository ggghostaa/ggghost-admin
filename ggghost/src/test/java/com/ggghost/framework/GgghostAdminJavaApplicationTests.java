package com.ggghost.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggghost.framework.entity.SysRole;
import com.ggghost.framework.entity.SysUser;
import com.ggghost.framework.password.BCryptPasswordEncoder;
import com.ggghost.framework.service.impl.RedisService;
import com.ggghost.framework.utlis.JwtUtils;
import jakarta.annotation.Resource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.AntPathMatcher;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class GgghostAdminJavaApplicationTests {
    @Autowired
    RedisService redisService;
    @Autowired
    JwtUtils jwtUtils;

    @Test
    void lock() {
        jwtUtils.a();
    }

    @Test
    void l1() throws JsonProcessingException {

    }

    @Test
    void createToken() {
    }

    @Test
    void validateToken() {
        String salt = "86958ced";
        String password = "123456";
        String username = "admin";
        String string = new SimpleHash("MD5", password, ByteSource.Util.bytes(salt), 1024).toString();
        System.out.println(string);
    }
    @Test
    void login() {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken("admin", "123456"));
    }

}
