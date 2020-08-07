package com.jobeth.perm.service;

import com.jobeth.perm.dto.PermissionDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jobeth.perm.po.Permission;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Jobeth
 * @since 2020-06-30
 */
public interface PermissionService extends IService<Permission> {

    /**
     * List<Permission>查询
     *
     * @param dto PermissionDTO
     * @return List<Permission>
     */
    List<Permission> listByPermissionDTO(PermissionDTO dto);

}
