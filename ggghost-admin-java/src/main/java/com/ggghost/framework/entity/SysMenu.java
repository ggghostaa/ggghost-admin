package com.ggghost.framework.entity;

import com.ggghost.framework.svo.AbstractEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 *@Author: ggghost
 *@CreateTime: 2024-07-12
 *@Description: 菜单实体类
 *@Version: 1.0
 */
@Entity
@Table(name = "t_sys_menu", schema = "sys")
public class SysMenu extends AbstractEntity {
    @Column(length = 50)
    private String name;//功能名
    @Column(length = 50)
    private String url;//引用路径
    @Column(length = 1)
    private String status;//状态
    private String icon;//菜单图标
    private String description;//描述
    @Column(nullable = false)
    private Integer seq;//顺序
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private SysMenu parent;//父级实体类
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SysMenu> children;//子菜单
    @Column(nullable = false)
    private Boolean isVisible = Boolean.TRUE;

    public void removeChild(SysMenu sysMenu) {
        if (children != null) {
            children.remove(sysMenu);
        }
    }

    public void addChild(SysMenu sysMenu) {
        if (children == null) children = new ArrayList<>();
        children.add(sysMenu);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public SysMenu getParent() {
        return parent;
    }

    public void setParent(SysMenu parent) {
        this.parent = parent;
    }

    public List<SysMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }
}
