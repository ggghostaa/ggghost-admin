package com.ggghost.framework.entity;

import com.ggghost.framework.svo.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

/**
 *@Author: ggghost
 *@CreateTime: 2024-07-12
 *@Description: 接口实体类
 *@Version: 1.0
 */
@Entity
@Table(name = "t_sys_permission", schema = "sys")
public class SysPermission extends AbstractEntity {
    private String permission;//权限标识
    private String name;//权限名称
    private String keyword;
    private String description;//描述
    private Date createTime;
    private Date updateTime;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
