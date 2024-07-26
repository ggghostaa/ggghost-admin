package com.ggghost.framework.exception;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-17
 * @Description:
 * @Version: 1.0
 */
public interface BaseErrorInfoInterface {
    /**
     * 错误描述
     * @return
     */
    String getErrorMsg();

    /**
     * 错误码
     * @return
     */
    int getErrorCode();
}
