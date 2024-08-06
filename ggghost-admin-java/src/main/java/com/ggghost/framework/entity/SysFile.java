package com.ggghost.framework.entity;

import com.ggghost.framework.svo.AbstractEntity;
import jakarta.persistence.*;

/**
 * @Author: ggghost
 * @CreateTime: 2024/8/6 10:33
 * @Description： 文件实体类
 * @Version: 1.0
 */
@Entity
@Table(name = "t_sys_file", schema = "sys")
public class SysFile extends AbstractEntity {
    @Column(length = 60)
    private String name;//文件名称
    private String path;//文件路径
    private long size;//文件大小
    @Column(length = 20)
    private String type;//文件类型
    @ManyToOne
    @JoinColumn(name = "sys_file_resource_id")
    private SysFileResource sysFileResource;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public SysFileResource getFileResource() {
        return sysFileResource;
    }

    public void setFileResource(SysFileResource sysFileResource) {
        this.sysFileResource = sysFileResource;
    }
}
