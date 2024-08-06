package com.ggghost.framework.file;

import com.ggghost.framework.entity.SysFile;

import java.io.InputStream;

/**
 * @Author: ggghost
 * @CreateTime: 2024/8/6 12:30
 * @Description: file upload service
 * @Version: 1.0
 */
public interface FileUploadService {
    /**
     * 初始化数据存储
     */
    void doInit();

    /**
     * 上传文件
     * @param is
     * @return
     */
    SysFile upload(InputStream is);

    /**
     * 删除文件
     * @param file
     * @return
     */
    boolean removeFile(SysFile file);

    /**
     * 前置处理 文件压缩等
     * @param is
     */
    void doBefore(InputStream is);

    /**
     * 后置处理
     * @param is
     */
    void doAfter(InputStream is);
}
