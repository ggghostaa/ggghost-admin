package com.ggghost.framework.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.lang.util.ByteSource;

/**
 *@Author: ggghost
 *@CreateTime: 2024-07-12
 *@Description: 密码比较器
 *@Version: 1.0
 */
public class GCredentialsMatcher extends SimpleCredentialsMatcher {
    private int hashIterations = 1024;

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String pwd = new String(upToken.getPassword());
        SimpleAuthenticationInfo simpleAuthenticationInfo = (SimpleAuthenticationInfo)info;
        ByteSource credentialsSalt = simpleAuthenticationInfo.getCredentialsSalt();
        SimpleHash simpleHash = new SimpleHash("MD5", pwd, credentialsSalt, hashIterations);
        String newPwd = simpleHash.toString();
        String old = info.getCredentials().toString();
        return equals(old, newPwd);
    }
}
