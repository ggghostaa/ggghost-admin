package com.ggghost.framework.entity;

import com.ggghost.framework.svo.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

/**
 * @Author: ggghost
 * @CreateTime: 2024/8/6 10:09
 * @Description: 文件存储实体类
 * @Version: 1.0
 */
@Entity
@Table(name = "t_sys_file_resource", schema = "sys")
public class SysFileResource extends AbstractEntity {
    @Column(length = 64)
    private String name;//存储名称
    private String implName;//实现类名
    private String implPath;//实现类路径
    private String accessKey;//accessKey
    private String secretKey;//secretKey
    private String bucket;//bucket
    private String path;//访问路径
    @Column(length = 20)
    private String createUser;
    private String createUserId;
    private Date createTime;
    @Column(length = 20)
    private String updateUser;
    private String updateUserId;
    private Date updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImplName() {
        return implName;
    }

    public void setImplName(String implName) {
        this.implName = implName;
    }

    public String getImplPath() {
        return implPath;
    }

    public void setImplPath(String implPath) {
        this.implPath = implPath;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }
}
