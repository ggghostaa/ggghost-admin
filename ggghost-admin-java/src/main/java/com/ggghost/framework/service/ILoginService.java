package com.ggghost.framework.service;

import com.ggghost.framework.dto.LoginUser;
import com.ggghost.framework.dto.ResponseInfo;
import com.ggghost.framework.entity.SysUser;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-15
 * @Description: 登录、注册接口
 * @Version: 1.0
 */
public interface ILoginService {
    /**
     * 登录
     * @param loginUser
     * @return
     */
    ResponseInfo login(LoginUser loginUser);

    /**
     * 注册
     * @param sysUser
     * @return
     */
    ResponseInfo register(LoginUser user);

    /**
     * 创建token
     * @param user
     * @return
     */
    String createToken(LoginUser user);

}
