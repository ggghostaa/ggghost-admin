package com.ggghost.framework.shiro;

import com.ggghost.framework.utlis.JwtUtils;
import org.apache.shiro.authc.AuthenticationToken;

import java.util.UUID;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-22
 * @Description: token实体类
 * @Version: 1.0
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
