package com.ggghost.framework.service;

import com.ggghost.framework.entity.SysUser;

/**
 *@Author: ggghost
 *@CreateTime: 2024-07-12
 *@Description:
 *@Version: 1.0
 */
public interface ISysUserService {
    SysUser findUserByUsername(String username);
}
