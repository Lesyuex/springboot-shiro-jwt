package com.jobeth.perm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jobeth.perm.dto.PermissionDTO;
import com.jobeth.perm.po.Permission;

import java.util.List;
/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Jobeth
 * @since 2020-06-30
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 查询权限
     *
     * @param dto dto
     * @return List<Permission>
     */
    List<Permission> listByPermissionDTO(PermissionDTO dto);
}
