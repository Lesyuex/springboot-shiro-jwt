package com.jobeth.perm.service.impl;

import com.jobeth.perm.po.Role;
import com.jobeth.perm.mapper.RoleMapper;
import com.jobeth.perm.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author Jobeth
 * @since 2020-06-30
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<Role> listByUserId(Long userId) {
        return null;
    }
}
