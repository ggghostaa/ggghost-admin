package com.ggghost.framework.view;

import com.ggghost.framework.svo.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * @Author: ggghost
 * @CreateTime: 2024/8/6 10:49
 * @Description: 文件视图
 * @Version: 1.0
 */
@Entity
@Table(name = "sys_file_view", schema = "sys")
public class SysFileView extends AbstractEntity {
    private String filePath;//文件路径
    private long size;//文件大小
    @Column(length = 20)
    private String type;//文件类型

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
