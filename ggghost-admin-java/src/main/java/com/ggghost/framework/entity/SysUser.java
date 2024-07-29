package com.ggghost.framework.entity;

import com.ggghost.framework.svo.AbstractEntity;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * 系统用户表
 */

@Entity
@Table(name = "t_sys_user", schema = "sys")
public class SysUser extends AbstractEntity {
    @Column(length = 20)
    private String username;//用户名
    @Column(length = 100)
    private String password;//密码
    @Column(length = 50)
    private String email;//邮箱
    @Column(length = 30)
    private String realName;//别称
    @Column(length = 30)
    private String phone;//手机
    @Column(nullable = false)
    private int status;//状态 0 禁用 | 1 启用
    private String remark;//备注
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
    private Date lastLoginTime;//最后登录时间
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinTable(name = "t_sys_user_role", schema = "sys",
    joinColumns = {@JoinColumn(name = "sys_user_id")},
    inverseJoinColumns = {@JoinColumn(name = "sys_role_id")})
    private List<SysRole> sysRoleList;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public List<SysRole> getSysRoleList() {
        return sysRoleList;
    }

    public void setSysRoleList(List<SysRole> sysRoleList) {
        this.sysRoleList = sysRoleList;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SysUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", lastLoginTime=" + lastLoginTime +
                ", sysRoleList=" + sysRoleList +
                '}';
    }
}
