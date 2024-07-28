package com.ggghost.framework.dto;

import com.ggghost.framework.entity.SysUser;
import com.ggghost.framework.valid.ValidGroup;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;

/**
 *@Author: ggghost
 *@CreateTime: 2024-07-12
 *@Description: 登录用户
 *@Version: 1.0
 */
public class LoginUser implements Serializable {
    private String id;
    @Pattern(regexp = "^[a-zA-Z0-9]{1,20}$", message = "用户名不能包含特殊字符，不能超过20个字符", groups = {ValidGroup.register.class})
    @NotBlank(message = "用户名不能为空")
    private String username;//用户名
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "密码至少包含8个字符。包括大写字母、小写字母、数字和特殊字符中的至少三种", groups = {ValidGroup.register.class})
    @NotBlank(message = "密码不能为空")
    private String password;//密码
    private String email;//邮箱
    private String realName;//别称
    private String phone;//手机
    private String status;//状态
    private String remark;//备注
    private String token;//token
    private String code;
    private String tokenId;

    public LoginUser(SysUser sysUser) {
        this.id = sysUser.getId();
        this.username = sysUser.getUsername();
        this.email = sysUser.getEmail();
        this.realName = sysUser.getRealName();
        this.phone = sysUser.getPhone();
        this.status = sysUser.getStatus();
        this.remark = sysUser.getRemark();
    }

    public LoginUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
