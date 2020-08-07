package com.jobeth.perm.vo;

import java.util.List;

/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/6/30 14:36
 */
public class MenuVO implements TreeVO<MenuVO> {
    /**
     * 权限id
     */
    private Integer id;

    /**
     * 菜单名
     */
    private String name;

    /**
     * 资源路径
     */
    private String path;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 组件资源(用于匹配component组件)
     */
    private String component;

    /**
     * 子菜单
     */
    private List<MenuVO> children;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    @Override
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public List<MenuVO> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<MenuVO> childrenList) {
        this.children = childrenList;
    }
}
