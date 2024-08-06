package com.ggghost.framework.file;

import com.ggghost.framework.entity.SysFile;
import org.apache.catalina.webresources.FileResource;

import java.io.InputStream;

/**
 * @Author: ggghost
 * @CreateTime: 2024/8/6 14:38
 * @Description: file upload resource
 * @Version: 1.0
 */
public interface FileUploadResource {

    /**
     * 获取对应配置
     * @return
     */
    FileResource getFileResource();

    /**
     * 认证
     * @return
     */
    String doAuthentication();

    /**
     * 上传文件
     * @param is
     * @return
     */
    String doUpload(InputStream is);

    /**
     * 删除文件
     * @param sysFile
     * @return
     */
    boolean removeFile(SysFile sysFile);
}
