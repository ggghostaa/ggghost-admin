package com.ggghost.framework.entity.log;

import com.ggghost.framework.entity.SysUser;
import com.ggghost.framework.svo.AbstractEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_api_log", schema = "log")
public class ApiLog extends AbstractEntity {
    private String apiUrl;//请求url
    private String apiUri;//请求url
    @Column(length = 50)
    private String className;//类名
    @Column(length = 50)
    private String method;//方法名
    @Column(columnDefinition = "TEXT")
    private String requestParams;//请求参数
    @Column(columnDefinition = "TEXT")
    private String responseParams;//响应结果
    private LocalDateTime beginTime;//请求时间
    private LocalDateTime endTime;//响应时间
    private double duration;//消耗时间
    private String ip;//ip
    private String userAgent;//客户端
    private String ipInfo;//归属地
    private String errMsg;//错误信息
    private String statusCode;//状态代码

    public String getApiUri() {
        return apiUri;
    }

    public void setApiUri(String apiUri) {
        this.apiUri = apiUri;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResponseParams() {
        return responseParams;
    }

    public void setResponseParams(String responseParams) {
        this.responseParams = responseParams;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpInfo() {
        return ipInfo;
    }

    public void setIpInfo(String ipAddress) {
        this.ipInfo = ipAddress;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

}
