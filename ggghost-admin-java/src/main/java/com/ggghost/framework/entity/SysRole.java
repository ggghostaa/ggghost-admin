package com.ggghost.framework.entity;

import com.ggghost.framework.svo.AbstractEntity;
import jakarta.persistence.*;

import java.util.List;

/**
 *@Author: ggghost
 *@CreateTime: 2024-07-12
 *@Description: 角色实体类
 *@Version: 1.0
 */
@Entity
@Table(name = "t_sys_role", schema = "sys")
public class SysRole extends AbstractEntity {
    @Column(length = 20)
    private String roleName;//角色名
    private String keyword;
    private String description;//描述
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "t_sys_role_menu", schema = "sys",
            joinColumns = {@JoinColumn(name = "sys_role_id")},
            inverseJoinColumns = {@JoinColumn(name = "sys_menu_id")}
    )
    private List<SysMenu> sysMenuList;//菜单列表
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "t_sys_role_permission", schema = "sys",
        joinColumns = {@JoinColumn(name = "sys_role_id")},
        inverseJoinColumns = {@JoinColumn(name = "sys_permission_id")}
    )
    private List<SysPermission> sysPermissionList;//接口列表

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<SysMenu> getSysMenuList() {
        return sysMenuList;
    }

    public void setSysMenuList(List<SysMenu> sysMenuList) {
        this.sysMenuList = sysMenuList;
    }


    public List<SysPermission> getSysPermissionList() {
        return sysPermissionList;
    }

    public void setSysPermissionList(List<SysPermission> sysPermissionList) {
        this.sysPermissionList = sysPermissionList;
    }
}
