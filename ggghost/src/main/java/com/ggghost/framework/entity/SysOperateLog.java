package com.ggghost.framework.entity;

import com.ggghost.framework.svo.AbstractEntity;
import jakarta.persistence.*;

import java.util.Date;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-17
 * @Description: 操作日志实体类
 * @Version: 1.0
 */
@Entity
@Table(name = "t_sys_operate_log", schema = "sys")
public class SysOperateLog extends AbstractEntity {
    private String module;//模块
    private String type;//操作类型
    private String interfaceName;//接口
    private Date operateTime;//操作时间
    private String operateContent;//操作内容
    private String operateUserName;//操作用户
    private String operateIp;//操作ip
    private String operateUrl;//操作url
    private String operateResult;//操作结果
    private String operateParams;//请求参数
    private String operateError;
    private Long elapsed_time;//操作耗时毫秒
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "sys_user_id")
    private SysUser sysUser;

    public String getOperateParams() {
        return operateParams;
    }

    public void setOperateParams(String operateParams) {
        this.operateParams = operateParams;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateContent() {
        return operateContent;
    }

    public void setOperateContent(String operateContent) {
        this.operateContent = operateContent;
    }

    public String getOperateUserName() {
        return operateUserName;
    }

    public void setOperateUserName(String operateUserName) {
        this.operateUserName = operateUserName;
    }

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp;
    }

    public String getOperateUrl() {
        return operateUrl;
    }

    public void setOperateUrl(String operateUrl) {
        this.operateUrl = operateUrl;
    }

    public String getOperateResult() {
        return operateResult;
    }

    public void setOperateResult(String operateResult) {
        this.operateResult = operateResult;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }
}
