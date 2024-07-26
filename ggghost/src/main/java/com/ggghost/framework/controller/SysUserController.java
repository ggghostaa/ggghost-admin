package com.ggghost.framework.controller;

import com.ggghost.framework.entity.SysUser;
import com.ggghost.framework.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-11
 * @Description:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/sys_user")
public class SysUserController {
    @Autowired
    SysUserRepository sysUserRepository;

    @RequestMapping("add")
    @ResponseBody
    public String add() {
        for (int i = 0; i < 100; i++) {
            SysUser sysUser = new SysUser();
            sysUser.setUsername("test" + i +1);
            sysUser.setPassword("123456");
            sysUser.setStatus("1");
            sysUserRepository.save(sysUser);
        }
        return "sys_user/add";
    }
}
