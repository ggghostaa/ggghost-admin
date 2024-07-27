package com.ggghost.framework.shiro.credentialsMatcher;

import com.ggghost.framework.shiro.JwtToken;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-25
 * @Description: token&#x6BD4;&#x8F83;&#x5668;
 * @Version: 1.0
 */
public class JwtCredentialsMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        return authenticationToken instanceof JwtToken;
    }
}
