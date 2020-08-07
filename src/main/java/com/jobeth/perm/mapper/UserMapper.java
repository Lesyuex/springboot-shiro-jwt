package com.jobeth.perm.mapper;

import com.jobeth.perm.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Jobeth
 * @since 2020-06-30
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查找
     * @param username username
     * @return User
     */
    User findByUsername(String username);
}
