package com.ggghost.framework.shiro;

import com.ggghost.framework.constant.RedisConstant;
import com.ggghost.framework.dto.LoginUser;
import com.ggghost.framework.entity.SysPermission;
import com.ggghost.framework.entity.SysRole;
import com.ggghost.framework.entity.SysUser;
import com.ggghost.framework.exception.BaseException;
import com.ggghost.framework.exception.user.UserAuthenticationException;
import com.ggghost.framework.exception.user.UserNotFoundException;
import com.ggghost.framework.service.ISysUserService;
import com.ggghost.framework.service.impl.RedisService;
import com.ggghost.framework.utlis.IpAddrUtils;
import com.ggghost.framework.utlis.JwtUtils;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        //判断token是否需要刷新
        boolean refresh = false;
        try {
            refresh = jwtUtils.isRefresh(token);
        } catch (Exception e) {
            //token不合法
            throw new UserAuthenticationException();
        }

        if (refresh) {//刷新token
            RLock rLock = redisService.getRLock(RedisConstant.LOCK + token);
            try {
                if (rLock.tryLock()) {
                    HttpServletRequest request = ((ServletRequestAttributes)
                            RequestContextHolder.getRequestAttributes()).getRequest();
                    String userAgent = IpAddrUtils.getUserAgentKey(request);
                    token = refreshToken(token, userAgent);
                }
            } catch (Exception e) {
                log.error("token refresh error\n\r {}", e.getMessage());
                throw new UserAuthenticationException();
            } finally {
                rLock.unlock();
            }
        }
        String username = jwtUtils.getClaimFiled(token, "username");
        SysUser user = userService.findUserByUsername(username);
        System.out.println(this.getName());
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, token, getName());
        return simpleAuthenticationInfo;
    }

    /**
     * 刷新token
     * @param token
     * @param userAgent 用户特征
     * @return
     */
    private String refreshToken(String token, String userAgent) {
        LoginUser loginUser = redisService.<LoginUser>get(RedisConstant.JWT_LOGIN_USER + userAgent + token);

        if (loginUser == null) {
            //抛出重新登录异常
            throw new UserAuthenticationException();
        }
        if (token.equals(loginUser.getToken())) {
//            String newToken = jwtUtils.encoding(loginUser);
            String newToken = "";
            loginUser.setToken(newToken);
            redisService.put(RedisConstant.JWT_LOGIN_USER + userAgent + token, loginUser, Duration.ofSeconds(30));
            redisService.put(RedisConstant.JWT_LOGIN_USER + userAgent + newToken, loginUser, Duration.ofDays(7));
            return newToken;
        } else {//token已刷新
            return loginUser.getToken();
        }
    }

}
