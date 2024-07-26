package com.ggghost.framework.custom;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 日志配置
 */
@Component
@ConfigurationProperties(prefix = "logging", ignoreUnknownFields = false)
public class LoggingProperties {
    private List<String> includePaths;
    private List<String> excludePaths;

    public List<String> getIncludePaths() {
        return includePaths;
    }

    public void setIncludePaths(List<String> includePaths) {
        this.includePaths = includePaths;
    }

    public List<String> getExcludePaths() {
        return excludePaths;
    }

    public void setExcludePaths(List<String> excludePaths) {
        this.excludePaths = excludePaths;
    }
}
