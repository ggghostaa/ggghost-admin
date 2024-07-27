package com.ggghost.framework.shiro.realm;

import com.ggghost.framework.entity.SysPermission;
import com.ggghost.framework.entity.SysRole;
import com.ggghost.framework.entity.SysUser;
import com.ggghost.framework.service.ISysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

/**
 *@Author: ggghost
 *@CreateTime: 2024-07-12
 *@Description: 自定义realm
 *@Version: 1.0
 */
@Component
public class LoginRealm extends AuthorizingRealm {
    private static final Logger logger = Logger.getLogger(LoginRealm.class.getName());
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 授权逻辑
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SysUser user = (SysUser) principalCollection.getPrimaryPrincipal();
        List<SysRole> sysRoleList = user.getSysRoleList();
        for (SysRole sysRole : sysRoleList) {
            authorizationInfo.addRole(sysRole.getRoleName());
            for (SysPermission permission : sysRole.getSysPermissionList()) {
                authorizationInfo.addStringPermission(permission.getKeyword());
            }
        }
        return authorizationInfo;
    }

    /**
     * 认证逻辑
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken)authenticationToken;
        SysUser user = sysUserService.findUserByUsername(upToken.getUsername());
        if (user == null) {
            return null;
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
    }
}
