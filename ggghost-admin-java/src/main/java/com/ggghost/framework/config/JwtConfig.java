package com.ggghost.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-11
 * @Description: jwt配置
 * @Version: 1.0
 */
@Component
@ConfigurationProperties(prefix = "token")
public class JwtConfig {
    private String secret;
    private String expiration;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}
