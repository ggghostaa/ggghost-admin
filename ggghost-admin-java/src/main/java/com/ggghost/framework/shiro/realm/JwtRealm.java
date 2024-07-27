package com.ggghost.framework.shiro.realm;

import com.ggghost.framework.constant.RedisConstant;
import com.ggghost.framework.dto.LoginUser;
import com.ggghost.framework.entity.SysPermission;
import com.ggghost.framework.entity.SysRole;
import com.ggghost.framework.entity.SysUser;
import com.ggghost.framework.exception.user.UserAuthenticationException;
import com.ggghost.framework.service.ISysUserService;
import com.ggghost.framework.service.impl.RedisService;
import com.ggghost.framework.shiro.JwtToken;
import com.ggghost.framework.utlis.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-22
 * @Description: jwt拦截器
 * @Version: 1.0
 */
@Component
public class JwtRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(JwtRealm.class);
    @Autowired
    ISysUserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    JwtUtils jwtUtils;
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }
    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SysUser user = (SysUser) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (SysRole sysRole : user.getSysRoleList()) {
            simpleAuthorizationInfo.addRole(sysRole.getRoleName());
            for (SysPermission permission : sysRole.getSysPermissionList()) {
                simpleAuthorizationInfo.addStringPermission(permission.getKeyword());
            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * jwt认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        LoginUser user = redisService.<LoginUser>get(RedisConstant.JWT + token);
        if (user == null) {
            Claims claims = jwtUtils.decoding(token);
            SysUser sysUser = userService.findUserByUsername((String) claims.get("username"));
            if (sysUser != null) user = new LoginUser(sysUser);
        }

        return new SimpleAuthenticationInfo(user, token, getName());
    }
}
