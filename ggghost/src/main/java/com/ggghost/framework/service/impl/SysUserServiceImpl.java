package com.ggghost.framework.service.impl;

import com.ggghost.framework.entity.SysUser;
import com.ggghost.framework.repository.SysUserRepository;
import com.ggghost.framework.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *@Author: ggghost
 *@CreateTime: 2024-07-12
 *@Description:
 *@Version: 1.0
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    @Autowired
    private SysUserRepository sysUserRepository;
    @Override
    public SysUser findUserByUsername(String username) {
        return sysUserRepository.findUserByUsername(username);
    }
}
