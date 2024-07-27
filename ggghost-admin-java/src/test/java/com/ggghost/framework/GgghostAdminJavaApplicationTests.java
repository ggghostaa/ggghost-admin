package com.ggghost.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ggghost.framework.service.impl.RedisService;
import com.ggghost.framework.utlis.JwtUtils;
import com.ggghost.framework.utlis.bcrypt.BCryptPasswordEncoder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GgghostAdminJavaApplicationTests {
    @Autowired
    RedisService redisService;
    @Autowired
    JwtUtils jwtUtils;

    @Test
    void lock() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123456"));
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
