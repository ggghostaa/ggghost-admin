package com.ggghost.framework.shiro.credentialsMatcher;

import com.ggghost.framework.utlis.bcrypt.BCryptPasswordEncoder;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 *@Author: ggghost
 *@CreateTime: 2024-07-12
 *@Description: 密码比较器
 *@Version: 1.0
 */
public class BCryptCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String pwd = new String(upToken.getPassword());
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        System.out.println(pwd);
        System.out.println(info.getCredentials().toString());
        return bCrypt.matches(pwd, info.getCredentials().toString());
    }
}
