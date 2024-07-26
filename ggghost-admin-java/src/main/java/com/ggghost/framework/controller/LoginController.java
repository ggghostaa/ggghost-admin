package com.ggghost.framework.controller;

import com.ggghost.framework.annotations.OperationLog;
import com.ggghost.framework.custom.LoggingProperties;
import com.ggghost.framework.dto.LoginUser;
import com.ggghost.framework.dto.ResponseInfo;
import com.ggghost.framework.entity.SysUser;
import com.ggghost.framework.service.ILoginService;
import com.ggghost.framework.utlis.IpAddrUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *@Author: ggghost
 *@CreateTime: 2024-07-12
 *@Description:登录controller
 *@Version: 1.0
 */
@Controller
@ResponseBody
public class LoginController {

    @Autowired
    ILoginService loginService;
    @Autowired
    LoggingProperties loggingProperties;

    /**
     * 登录
     * @param loginUser
     * @return
     */
    @RequestMapping("/login")
    @OperationLog(module = "user")
    public ResponseInfo login(@RequestBody LoginUser loginUser) {
        return loginService.login(loginUser);
    }

    /**
     * 注册
     * @param sysUser
     * @return
     */
    @RequestMapping("/register")
    public ResponseInfo register(@RequestBody SysUser sysUser) {
        return loginService.register(sysUser);
    }

    @RequestMapping("/test")
    public ResponseInfo test() {
        String userAgentKey = IpAddrUtils.getUserAgentKey();
        return ResponseInfo.success("");
    }
}
