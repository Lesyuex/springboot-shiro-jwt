package com.jobeth.perm.dto;


import lombok.Builder;
import lombok.Data;

/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/8/2 0:23
 */
@Data
@Builder
public class PermissionDTO {

    /**
     * 权限名
     */
    private String name;

    /**
     * 资源类型:0:请求资源，1：菜单
     */
    private Integer type;

    /**
     * 资源路径或路由路径
     */
    private String url;

    private Integer status;

    private Long userId;
    private Long roleId;
}
